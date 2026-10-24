package com.example.smartpantrymanager;

import java.util.Locale;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private Button btnBackFromRecipeDetail;
    private TextView tvRecipeName;
    private TextView tvRecipeDescription;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        EdgeToEdgeHelper.apply(this);

        btnBackFromRecipeDetail = findViewById(R.id.btnBackFromRecipeDetail);

        btnBackFromRecipeDetail.setOnClickListener(v -> finish());

        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients);
        tvRecipeInstructions = findViewById(R.id.tvRecipeInstructions);

        String recipeName =
                getIntent().getStringExtra("recipe_name");

        String recipeDescription =
                getIntent().getStringExtra("recipe_description");

        String recipeIngredients =
                getIntent().getStringExtra("recipe_ingredients");

        String recipeInstructions =
                getIntent().getStringExtra("recipe_instructions");

        tvRecipeName.setText(recipeName);
        tvRecipeDescription.setText(recipeDescription);
        tvRecipeIngredients.setText(formatIngredients(recipeIngredients));
        tvRecipeInstructions.setText(recipeInstructions);
    }

    private String formatIngredients(String ingredients) {

        if (ingredients == null || ingredients.trim().isEmpty()) {
            return "No ingredients listed.";
        }

        StringBuilder formatted = new StringBuilder();

        String[] ingredientList = ingredients.split(",");

        for (String ingredient : ingredientList) {

            String[] parts = ingredient.split(":", 3);

            if (parts.length != 3) {
                formatted.append(ingredient.trim()).append("\n");
                continue;
            }

            String name = parts[0].trim();
            String quantity = parts[1].trim();
            String unit = parts[2].trim();

            if (name.length() > 0) {
                name = name.substring(0, 1).toUpperCase(Locale.ROOT)
                        + name.substring(1);
            }

            String displayUnit = unit;

            if (quantity.equals("1")) {
                if (unit.equalsIgnoreCase("items")) {
                    displayUnit = "item";
                } else if (unit.equalsIgnoreCase("grams")) {
                    displayUnit = "gram";
                } else if (unit.equalsIgnoreCase("ml")) {
                    displayUnit = "ml";
                } else if (unit.equalsIgnoreCase("litres")
                        || unit.equalsIgnoreCase("l")) {
                    displayUnit = "litre";
                }
            }

            formatted.append("• ")
                    .append(name)
                    .append(" — ")
                    .append(quantity)
                    .append(" ")
                    .append(displayUnit)
                    .append("\n");
        }

        return formatted.toString().trim();
    }
}