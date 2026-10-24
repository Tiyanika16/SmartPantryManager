package com.example.smartpantrymanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recipesListView;
    private TextView noRecipesNotice;
    private Button btnBackToPantry;

    private DatabaseHelper dbHelper;
    private RecipeAdapter adapter;
    private final List<Recipe> availableRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        EdgeToEdgeHelper.apply(this);

        recipesListView = findViewById(R.id.recyclerViewRecipes);
        noRecipesNotice = findViewById(R.id.tvNoRecipes);

        btnBackToPantry = findViewById(R.id.btnBackFromSuggestedRecipes);

        btnBackToPantry.setOnClickListener(v -> finish());

        dbHelper = DatabaseHelper.getInstance(this);

        setupRecyclerView();

        updateSuggestedRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHelper != null) {
            updateSuggestedRecipes();
        }
    }

    private void setupRecyclerView() {
        recipesListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(availableRecipes);
        recipesListView.setAdapter(adapter);
    }

    private void updateSuggestedRecipes() {
        availableRecipes.clear();

        List<Ingredient> pantryContents = getPantryIngredients();
        List<Recipe> allRecipes = getAllRecipesFromDatabase();

        for (Recipe r : allRecipes) {
            if (RecipeMatcher.isRecipeFeasible(r, pantryContents)) {
                availableRecipes.add(r);
            }
        }

        adapter.notifyDataSetChanged();
        toggleRecipeVisibility(availableRecipes.isEmpty());
    }

    private void toggleRecipeVisibility(boolean noRecipesAvailable) {
        if (noRecipesAvailable) {
            noRecipesNotice.setVisibility(View.VISIBLE);
            recipesListView.setVisibility(View.GONE);
        } else {
            noRecipesNotice.setVisibility(View.GONE);
            recipesListView.setVisibility(View.VISIBLE);
        }
    }

    private List<Ingredient> getPantryIngredients() {
        List<Ingredient> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                "ingredients", null,null,null,null,null,
                "name ASC")) {

            while (cursor.moveToNext()) {
                list.add(new Ingredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"))
                ));
            }
        }

        return list;
    }

    private List<Recipe> getAllRecipesFromDatabase() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                "recipes", null,null,null,null,null,
                "name ASC")) {

            while (cursor.moveToNext()) {
                list.add(new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ingredients")),
                        cursor.getString(cursor.getColumnIndexOrThrow("instructions"))
                ));
            }
        }

        return list;
    }
}
