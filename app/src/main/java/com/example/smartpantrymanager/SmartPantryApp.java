package com.example.smartpantrymanager;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class SmartPantryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Apply dark/light theme ONCE when the app starts up
        SharedPreferences preferences = getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}