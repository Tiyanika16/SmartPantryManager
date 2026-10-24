package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIngredients;
    private TextView tvEmptyMessage;
    private Button btnAddIngredient;
    private Button btnSuggestedRecipes;
    private Button btnSettings;
    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        EdgeToEdgeHelper.apply(this);

        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnSuggestedRecipes = findViewById(R.id.btnSuggestedRecipes);
        btnSettings = findViewById(R.id.btnSettings);
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });
        btnSuggestedRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PantryActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
        });
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    PantryActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });
        databaseHelper = DatabaseHelper.getInstance(this);

        ingredientList = new ArrayList<>();

        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(this)
        );

        ingredientAdapter = new IngredientAdapter(ingredientList, this::updateEmptyState);
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        loadIngredients();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }
    private void loadIngredients() {

        ingredientList.clear();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "ingredients",
                null,
                null,
                null,
                null,
                null,
                "name ASC"
        );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
            String expiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow("expiryDate")
            );

            Ingredient ingredient = new Ingredient(
                    id,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            ingredientList.add(ingredient);
        }

        cursor.close();

        ingredientAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (ingredientList.isEmpty()) {
            tvEmptyMessage.setVisibility(TextView.VISIBLE);
            recyclerViewIngredients.setVisibility(RecyclerView.GONE);
        } else {
            tvEmptyMessage.setVisibility(TextView.GONE);
            recyclerViewIngredients.setVisibility(RecyclerView.VISIBLE);
        }
    }
}