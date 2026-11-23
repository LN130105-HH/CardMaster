package com.cardmaster.app;

import android.app.Application;

import com.cardmaster.app.data.local.AppDatabase;

public class CardMasterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Room Database
        AppDatabase.getInstance(this);
    }
}
