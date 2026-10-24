package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBackFromSettings;
    private SwitchCompat switchExpiryReminders;
    private SwitchCompat switchDarkMode;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EdgeToEdgeHelper.apply(this);

        btnBackFromSettings = findViewById(R.id.btnBackFromSettings);
        switchExpiryReminders = findViewById(R.id.switchExpiryReminders);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        btnBackFromSettings.setOnClickListener(v -> finish());

        preferences = getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);

        // --- Expiry Reminders Setup ---
        boolean expiryRemindersEnabled = preferences.getBoolean("expiry_reminders", true);
        switchExpiryReminders.setChecked(expiryRemindersEnabled);
        updateExpirySwitchText(expiryRemindersEnabled);

        switchExpiryReminders.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("expiry_reminders", isChecked).apply();
            updateExpirySwitchText(isChecked);
        });

        // --- Dark Mode Setup ---
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);

        // 1. Set initial UI state without listener
        switchDarkMode.setChecked(isDarkMode);
        updateDarkModeSwitchText(isDarkMode);

        // 2. Add listener ONLY for direct user touches
        switchDarkMode.setOnClickListener(v -> {
            boolean isChecked = switchDarkMode.isChecked();
            boolean currentSaved = preferences.getBoolean("dark_mode", false);

            if (isChecked != currentSaved) {
                // Save preference
                preferences.edit().putBoolean("dark_mode", isChecked).apply();
                updateDarkModeSwitchText(isChecked);

                // Update theme ONCE
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void updateExpirySwitchText(boolean isEnabled) {
        switchExpiryReminders.setText(isEnabled ? "ON" : "OFF");
    }

    private void updateDarkModeSwitchText(boolean isEnabled) {
        switchDarkMode.setText(isEnabled ? "ON" : "OFF");
    }
}