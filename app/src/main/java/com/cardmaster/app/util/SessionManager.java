package com.cardmaster.app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "cardmaster_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_FIREBASE_UID = "firebase_uid";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOB = "date_of_birth";
    private static final String KEY_AVATAR_URI = "avatar_uri";
    private static final String KEY_GEMINI_API_KEY = "gemini_api_key";
    private static final String KEY_LAST_STUDIED_DECK_ID = "last_studied_deck_id";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void createLoginSession(long userId, String name, String email) {
        createLoginSession(userId, name, email, "");
    }

    public void createLoginSession(long userId, String name, String email, String firebaseUid) {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_FIREBASE_UID, firebaseUid)
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, 1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Hoàng Vũ Lê Huy");
    }

    public void setUserName(String name) {
        prefs.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "user@cardmaster.app");
    }

    public void setUserEmail(String email) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public String getFirebaseUid() {
        return prefs.getString(KEY_FIREBASE_UID, "");
    }

    public void setFirebaseUid(String uid) {
        prefs.edit().putString(KEY_FIREBASE_UID, uid).apply();
    }

    public String getGender() {
        return prefs.getString(KEY_GENDER, "Nam");
    }

    public void setGender(String gender) {
        prefs.edit().putString(KEY_GENDER, gender).apply();
    }

    public String getDateOfBirth() {
        return prefs.getString(KEY_DOB, "15/08/2003");
    }

    public void setDateOfBirth(String dob) {
        prefs.edit().putString(KEY_DOB, dob).apply();
    }

    public String getAvatarUri() {
        return prefs.getString(KEY_AVATAR_URI, "");
    }

    public void setAvatarUri(String uri) {
        prefs.edit().putString(KEY_AVATAR_URI, uri).apply();
    }

    public void setGeminiApiKey(String apiKey) {
        prefs.edit().putString(KEY_GEMINI_API_KEY, apiKey).apply();
    }

    public String getGeminiApiKey() {
        return prefs.getString(KEY_GEMINI_API_KEY, "");
    }

    public void setLastStudiedDeckId(long deckId) {
        prefs.edit().putLong(KEY_LAST_STUDIED_DECK_ID, deckId).apply();
    }

    public long getLastStudiedDeckId() {
        return prefs.getLong(KEY_LAST_STUDIED_DECK_ID, -1);
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
