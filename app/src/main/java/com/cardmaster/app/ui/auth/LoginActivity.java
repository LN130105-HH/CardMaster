package com.cardmaster.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.auth.FirebaseAuthManager;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.UserEntity;
import com.cardmaster.app.databinding.ActivityLoginBinding;
import com.cardmaster.app.ui.main.MainActivity;
import com.cardmaster.app.util.SessionManager;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;
    private FirebaseAuthManager authManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        authManager = new FirebaseAuthManager(this);
        db = AppDatabase.getInstance(this);

        binding.btnLogin.setOnClickListener(v -> performLogin());

        binding.btnQuickDemoLogin.setOnClickListener(v -> {
            sessionManager.createLoginSession(1, "Hoàng Vũ Lê Huy", "user@cardmaster.app", "demo_firebase_uid");
            sessionManager.setGender("Nam");
            sessionManager.setDateOfBirth("15/08/2003");
            goToMain();
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        binding.tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Vui lòng nhập email");
            return;
        } else {
            binding.tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Vui lòng nhập mật khẩu");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        binding.btnLogin.setEnabled(false);

        authManager.login(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(UserEntity user) {
                runOnUiThread(() -> {
                    binding.btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Đăng nhập Firebase thành công!", Toast.LENGTH_SHORT).show();
                    goToMain();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    // Fallback to local DB if offline
                    Executors.newSingleThreadExecutor().execute(() -> {
                        UserEntity localUser = db.userDao().findByEmail(email);
                        runOnUiThread(() -> {
                            binding.btnLogin.setEnabled(true);
                            if (localUser != null && password.equals(localUser.getPasswordHash())) {
                                sessionManager.createLoginSession(localUser.getId(), localUser.getFullName(), localUser.getEmail(), localUser.getFirebaseUid());
                                if (localUser.getGender() != null) sessionManager.setGender(localUser.getGender());
                                if (localUser.getDateOfBirth() != null) sessionManager.setDateOfBirth(localUser.getDateOfBirth());
                                Toast.makeText(LoginActivity.this, "Đăng nhập tài khoản cục bộ thành công!", Toast.LENGTH_SHORT).show();
                                goToMain();
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        });
                    });
                });
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
