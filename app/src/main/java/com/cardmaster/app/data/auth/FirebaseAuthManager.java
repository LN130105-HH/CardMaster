package com.cardmaster.app.data.auth;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.UserEntity;
import com.cardmaster.app.util.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executors;

public class FirebaseAuthManager {

    public interface AuthCallback {
        void onSuccess(UserEntity user);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    private final Context context;
    private final FirebaseAuth mAuth;
    private final AppDatabase db;
    private final SessionManager sessionManager;

    public FirebaseAuthManager(Context context) {
        this.context = context.getApplicationContext();
        this.mAuth = FirebaseAuth.getInstance();
        this.db = AppDatabase.getInstance(this.context);
        this.sessionManager = new SessionManager(this.context);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public boolean isFirebaseSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void register(String email, String password, String fullName, String gender, String dateOfBirth, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        String uid = fbUser != null ? fbUser.getUid() : "uid_" + System.currentTimeMillis();

                        if (fbUser != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();
                            fbUser.updateProfile(profileUpdates);
                        }

                        // Save user in local DB & session
                        Executors.newSingleThreadExecutor().execute(() -> {
                            long now = System.currentTimeMillis();
                            UserEntity user = db.userDao().findByEmail(email);
                            if (user == null) {
                                user = new UserEntity(uid, fullName, email, "", gender, dateOfBirth, now);
                                long id = db.userDao().insert(user);
                                user.setId(id);
                            } else {
                                user.setFirebaseUid(uid);
                                user.setFullName(fullName);
                                user.setGender(gender);
                                user.setDateOfBirth(dateOfBirth);
                                db.userDao().update(user);
                            }

                            sessionManager.createLoginSession(user.getId(), fullName, email, uid);
                            sessionManager.setGender(gender);
                            sessionManager.setDateOfBirth(dateOfBirth);

                            UserEntity finalUser = user;
                            callback.onSuccess(finalUser);
                        });
                    } else {
                        String errMsg = "Đăng ký thất bại";
                        if (task.getException() != null) {
                            errMsg = task.getException().getLocalizedMessage();
                            if (errMsg != null && errMsg.contains("already in use")) {
                                errMsg = "Email này đã được sử dụng cho một tài khoản khác.";
                            } else if (errMsg != null && errMsg.contains("network")) {
                                errMsg = "Lỗi kết nối mạng khi liên hệ với Firebase.";
                            }
                        }
                        callback.onError(errMsg);
                    }
                });
    }

    public void login(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        String uid = fbUser != null ? fbUser.getUid() : "uid_" + System.currentTimeMillis();
                        String name = (fbUser != null && !TextUtils.isEmpty(fbUser.getDisplayName()))
                                ? fbUser.getDisplayName() : "Người dùng";
                        String photoUrl = (fbUser != null && fbUser.getPhotoUrl() != null)
                                ? fbUser.getPhotoUrl().toString() : "";

                        Executors.newSingleThreadExecutor().execute(() -> {
                            UserEntity user = db.userDao().findByEmail(email);
                            long now = System.currentTimeMillis();
                            if (user == null) {
                                user = new UserEntity(uid, name, email, photoUrl, "Nam", "15/08/2003", now);
                                long id = db.userDao().insert(user);
                                user.setId(id);
                            } else {
                                user.setFirebaseUid(uid);
                                if (!TextUtils.isEmpty(photoUrl)) {
                                    user.setAvatarUri(photoUrl);
                                }
                                db.userDao().update(user);
                            }

                            sessionManager.createLoginSession(user.getId(), user.getFullName(), user.getEmail(), uid);
                            if (!TextUtils.isEmpty(user.getGender())) sessionManager.setGender(user.getGender());
                            if (!TextUtils.isEmpty(user.getDateOfBirth())) sessionManager.setDateOfBirth(user.getDateOfBirth());
                            if (!TextUtils.isEmpty(user.getAvatarUri())) sessionManager.setAvatarUri(user.getAvatarUri());

                            UserEntity finalUser = user;
                            callback.onSuccess(finalUser);
                        });
                    } else {
                        String errMsg = "Đăng nhập thất bại: Email hoặc mật khẩu không chính xác.";
                        if (task.getException() != null) {
                            String ex = task.getException().getMessage();
                            if (ex != null && ex.contains("network")) {
                                errMsg = "Lỗi kết nối mạng khi liên hệ với Firebase Authentication.";
                            }
                        }
                        callback.onError(errMsg);
                    }
                });
    }

    public void sendPasswordReset(String email, SimpleCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        String msg = task.getException() != null ? task.getException().getLocalizedMessage() : "Không thể gửi email đặt lại mật khẩu";
                        callback.onError(msg);
                    }
                });
    }

    public void updateProfile(String fullName, String avatarUri, String gender, String dateOfBirth, SimpleCallback callback) {
        FirebaseUser fbUser = mAuth.getCurrentUser();
        if (fbUser != null) {
            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName);
            if (!TextUtils.isEmpty(avatarUri)) {
                try {
                    builder.setPhotoUri(Uri.parse(avatarUri));
                } catch (Exception ignored) {}
            }
            fbUser.updateProfile(builder.build());
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            long userId = sessionManager.getUserId();
            UserEntity user = db.userDao().getById(userId);
            if (user != null) {
                user.setFullName(fullName);
                user.setAvatarUri(avatarUri);
                user.setGender(gender);
                user.setDateOfBirth(dateOfBirth);
                db.userDao().update(user);
            }

            sessionManager.setUserName(fullName);
            if (!TextUtils.isEmpty(avatarUri)) sessionManager.setAvatarUri(avatarUri);
            sessionManager.setGender(gender);
            sessionManager.setDateOfBirth(dateOfBirth);

            callback.onSuccess();
        });
    }

    public void logout() {
        try {
            mAuth.signOut();
        } catch (Exception ignored) {}
        sessionManager.logout();
    }
}
