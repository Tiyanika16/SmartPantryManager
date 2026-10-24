package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredientList;
    private final Runnable onListChanged;

    public IngredientAdapter(List<Ingredient> ingredientList, Runnable onListChanged) {
        this.ingredientList = ingredientList;
        this.onListChanged = onListChanged;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);

        // Ingredient name
        holder.tvIngredientName.setText(ingredient.getName());

        // Ingredient colour accent
        int ingredientColor = getIngredientColor(ingredient.getName());
        holder.ingredientColorAccent.setBackgroundColor(ingredientColor);

        // Quantity and unit
        String details = Ingredient.formatQuantity(ingredient.getQuantity()) + " " + ingredient.getUnit();
        holder.tvIngredientDetails.setText(details);

        // Expiry date
        String expiryDate = ingredient.getExpiryDate();

        if (expiryDate != null && !expiryDate.trim().isEmpty()) {

            holder.tvIngredientExpiry.setText("Expires: " + expiryDate);
            holder.tvIngredientExpiry.setVisibility(View.VISIBLE);

            SharedPreferences preferences = holder.itemView.getContext()
                    .getSharedPreferences(
                            "SmartPantrySettings",
                            Context.MODE_PRIVATE
                    );

            boolean expiryRemindersEnabled = preferences.getBoolean(
                    "expiry_reminders",
                    true
            );

            if (expiryRemindersEnabled) {

                String warning = getExpiryWarning(expiryDate);

                if (warning != null) {

                    holder.tvExpiryWarning.setText(warning);
                    holder.tvExpiryWarning.setVisibility(View.VISIBLE);

                } else {

                    holder.tvExpiryWarning.setText("");
                    holder.tvExpiryWarning.setVisibility(View.GONE);
                }

            } else {

                holder.tvExpiryWarning.setText("");
                holder.tvExpiryWarning.setVisibility(View.GONE);
            }

        } else {

            holder.tvIngredientExpiry.setText("");
            holder.tvIngredientExpiry.setVisibility(View.GONE);

            holder.tvExpiryWarning.setText("");
            holder.tvExpiryWarning.setVisibility(View.GONE);
        }

        // EDIT BUTTON
        holder.btnEditIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    AddIngredientActivity.class
            );

            intent.putExtra("ingredient_id", ingredient.getId());
            intent.putExtra("ingredient_name", ingredient.getName());
            intent.putExtra("ingredient_quantity", ingredient.getQuantity());
            intent.putExtra("ingredient_unit", ingredient.getUnit());
            intent.putExtra("ingredient_expiry", ingredient.getExpiryDate());

            v.getContext().startActivity(intent);
        });

        // DELETE BUTTON
        holder.btnDeleteIngredient.setOnClickListener(v -> {

            new MaterialAlertDialogBuilder(v.getContext())
                    .setTitle("Delete Ingredient")
                    .setMessage(
                            "Are you sure you want to delete "
                                    + ingredient.getName()
                                    + "?"
                    )
                    .setPositiveButton("Delete", (dialog, which) -> {

                        Context context = v.getContext();

                        DatabaseHelper databaseHelper =
                                DatabaseHelper.getInstance(context);

                        SQLiteDatabase db =
                                databaseHelper.getWritableDatabase();

                        db.delete(
                                "ingredients",
                                "id = ?",
                                new String[]{
                                        String.valueOf(ingredient.getId())
                                }
                        );


                        // Remove the ingredient from the list
                        // so it disappears from the screen.
                        int currentPosition = holder.getAdapterPosition();

                        if (currentPosition != RecyclerView.NO_POSITION) {

                            ingredientList.remove(currentPosition);

                            notifyItemRemoved(currentPosition);
                        }

                        if (onListChanged != null) {
                            onListChanged.run();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // Choose the accent colour based on the ingredient name
    private int getIngredientColor(String ingredientName) {

        String name = ingredientName.toLowerCase(Locale.ROOT).trim();

        if (name.equals("banana") || name.equals("bananas")) {
            return android.graphics.Color.parseColor("#D4A72C");
        }

        if (name.equals("egg") || name.equals("eggs")) {
            return android.graphics.Color.parseColor("#C99A24");
        }

        if (name.equals("tomato") || name.equals("tomatoes")) {
            return android.graphics.Color.parseColor("#C94C4C");
        }

        if (name.equals("chicken")) {
            return android.graphics.Color.parseColor("#C9783A");
        }

        if (name.equals("carrot") || name.equals("carrots")) {
            return android.graphics.Color.parseColor("#4F8A5B");
        }

        if (name.equals("cheese")) {
            return android.graphics.Color.parseColor("#D18B2C");
        }

        if (name.equals("milk")) {
            return android.graphics.Color.parseColor("#5B8DB8");
        }

        if (name.equals("bread")) {
            return android.graphics.Color.parseColor("#9A6842");
        }

        if (name.equals("pasta")) {
            return android.graphics.Color.parseColor("#7A5A9E");
        }

        if (name.equals("rice")) {
            return android.graphics.Color.parseColor("#B89B5E");
        }

        if (name.equals("onion") || name.equals("onions")) {
            return android.graphics.Color.parseColor("#8A5A9E");
        }

        if (name.equals("garlic")) {
            return android.graphics.Color.parseColor("#7A9A5A");
        }

        // Default colour for other ingredients
        return android.graphics.Color.parseColor("#6B4FA8");
    }

    private String getExpiryWarning(String expiryDate) {

        String[] dateFormats = {
                "dd/MM/yyyy",
                "yyyy-MM-dd",
                "MM/dd/yyyy"
        };

        Date expiry = null;

        for (String format : dateFormats) {

            try {

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat(
                                format,
                                Locale.getDefault()
                        );

                dateFormat.setLenient(false);

                expiry = dateFormat.parse(expiryDate);

                if (expiry != null) {
                    break;
                }

            } catch (ParseException ignored) {
                // Try the next date format.
            }
        }

        if (expiry == null) {
            return null;
        }

        // Compare whole calendar days (an item that expires TODAY is not yet expired).
        java.util.Calendar today = java.util.Calendar.getInstance();
        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
        today.set(java.util.Calendar.MINUTE, 0);
        today.set(java.util.Calendar.SECOND, 0);
        today.set(java.util.Calendar.MILLISECOND, 0);

        long difference = expiry.getTime() - today.getTimeInMillis();

        long daysRemaining = Math.floorDiv(difference, 1000L * 60L * 60L * 24L);

        if (daysRemaining < 0) {
            return "Expired!";
        }

        if (daysRemaining <= 3) {
            return "Expires soon!";
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientDetails;
        TextView tvIngredientExpiry;
        TextView tvExpiryWarning;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        View ingredientColorAccent;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName =
                    itemView.findViewById(R.id.tvIngredientName);

            tvIngredientDetails =
                    itemView.findViewById(R.id.tvIngredientDetails);

            tvIngredientExpiry =
                    itemView.findViewById(R.id.tvIngredientExpiry);

            tvExpiryWarning =
                    itemView.findViewById(R.id.tvExpiryWarning);

            btnEditIngredient =
                    itemView.findViewById(R.id.btnEditIngredient);

            btnDeleteIngredient =
                    itemView.findViewById(R.id.btnDeleteIngredient);

            ingredientColorAccent =
                    itemView.findViewById(R.id.ingredientColorAccent);
        }
    }
}