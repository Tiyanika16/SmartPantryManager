package com.example.smartpantrymanager;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipeList.get(position);

        String recipeName = recipe.getName();

        holder.tvRecipeName.setText(recipeName);

        holder.tvRecipeDescription.setText(
                recipe.getDescription()
        );

        int recipeColor = getRecipeColor(recipeName);

        holder.recipeColorAccent.setBackgroundColor(recipeColor);

        holder.tvRecipeName.setTextColor(recipeColor);

        holder.btnViewRecipe.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            v.getContext(),
                            RecipeDetailActivity.class
                    );

            intent.putExtra(
                    "recipe_id",
                    recipe.getId()
            );

            intent.putExtra(
                    "recipe_name",
                    recipe.getName()
            );

            intent.putExtra(
                    "recipe_description",
                    recipe.getDescription()
            );

            intent.putExtra(
                    "recipe_ingredients",
                    recipe.getIngredients()
            );

            intent.putExtra(
                    "recipe_instructions",
                    recipe.getInstructions()
            );

            v.getContext().startActivity(intent);
        });
    }

    private int getRecipeColor(String recipeName) {

        String name = recipeName.toLowerCase(Locale.ROOT);

        if (name.contains("tomato")) {
            return Color.parseColor("#C94C4C");
        }

        if (name.contains("banana")) {
            return Color.parseColor("#D4A72C");
        }

        if (name.contains("vegetable")) {
            return Color.parseColor("#4F8A5B");
        }

        if (name.contains("chicken")) {
            return Color.parseColor("#C9783A");
        }

        if (name.contains("tuna")) {
            return Color.parseColor("#3F78A8");
        }

        if (name.contains("egg")) {
            return Color.parseColor("#C99A24");
        }

        if (name.contains("cheese")) {
            return Color.parseColor("#D18B2C");
        }

        if (name.contains("toast")) {
            return Color.parseColor("#9A6842");
        }

        if (name.contains("pasta")) {
            return Color.parseColor("#7A5A9E");
        }

        return Color.parseColor("#6B4FA8");
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeDescription;
        Button btnViewRecipe;
        View recipeColorAccent;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );

            tvRecipeDescription =
                    itemView.findViewById(
                            R.id.tvRecipeDescription
                    );

            btnViewRecipe =
                    itemView.findViewById(
                            R.id.btnViewRecipe
                    );

            recipeColorAccent =
                    itemView.findViewById(
                            R.id.recipeColorAccent
                    );
        }
    }
}