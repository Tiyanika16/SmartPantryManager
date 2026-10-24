package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirect directly to PantryActivity and close MainActivity
        startActivity(new Intent(MainActivity.this, PantryActivity.class));
        finish();
    }
}
