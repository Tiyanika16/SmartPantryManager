package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddIngredientActivity extends AppCompatActivity {

    // Units shown in the dropdown. Must match what RecipeMatcher understands.
    private static final String[] UNITS = {"items", "grams", "kg", "ml", "litres"};
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private EditText etIngredientName;
    private EditText etQuantity;
    private Spinner spinnerUnit;
    private EditText etExpiryDate;
    private Button btnSaveIngredient;
    private Button btnBackFromAddIngredient;
    private TextView tvAddTitle;

    private DatabaseHelper databaseHelper;

    // ID of the ingredient being edited. -1 means we are adding a new ingredient.
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        EdgeToEdgeHelper.apply(this);

        btnBackFromAddIngredient = findViewById(R.id.btnBackFromAddIngredient);
        btnBackFromAddIngredient.setOnClickListener(v -> finish());

        tvAddTitle = findViewById(R.id.tvAddIngredientTitle);
        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        databaseHelper = DatabaseHelper.getInstance(this);

        spinnerUnit.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, UNITS));

        etExpiryDate.setOnClickListener(v -> showDatePicker());

        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId != -1) {
            etIngredientName.setText(getIntent().getStringExtra("ingredient_name"));
            etQuantity.setText(Ingredient.formatQuantity(
                    getIntent().getDoubleExtra("ingredient_quantity", 0)));
            selectUnit(getIntent().getStringExtra("ingredient_unit"));
            etExpiryDate.setText(getIntent().getStringExtra("ingredient_expiry"));

            tvAddTitle.setText("Edit Ingredient");
            btnSaveIngredient.setText("Update Ingredient");
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void selectUnit(String storedUnit) {
        if (storedUnit == null) return;
        String standard = RecipeMatcher.standardizeUnit(storedUnit);
        for (int i = 0; i < UNITS.length; i++) {
            if (RecipeMatcher.standardizeUnit(UNITS[i]).equals(standard)) {
                spinnerUnit.setSelection(i);
                return;
            }
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        Date existing = parseDate(etExpiryDate.getText().toString().trim());
        if (existing != null) cal.setTime(existing);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    Calendar picked = Calendar.getInstance();
                    picked.set(year, month, day);
                    etExpiryDate.setText(new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                            .format(picked.getTime()));
                    etExpiryDate.setError(null);
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Clear",
                (d, which) -> etExpiryDate.setText(""));
        dialog.show();
    }

    private Date parseDate(String text) {
        if (text.isEmpty()) return null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        format.setLenient(false);
        try {
            return format.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    private void saveIngredient() {
        String name = etIngredientName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = (String) spinnerUnit.getSelectedItem();
        String expiryDate = etExpiryDate.getText().toString().trim();

        if (name.isEmpty()) {
            etIngredientName.setError("Enter an ingredient name");
            etIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Enter a quantity");
            etQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than 0");
            etQuantity.requestFocus();
            return;
        }

        if (!expiryDate.isEmpty() && parseDate(expiryDate) == null) {
            etExpiryDate.setError("Use the date picker (dd/MM/yyyy)");
            etExpiryDate.requestFocus();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        // store NULL (not an empty string) when there is no expiry date
        if (expiryDate.isEmpty()) {
            values.putNull("expiryDate");
        } else {
            values.put("expiryDate", expiryDate);
        }

        long result;
        if (ingredientId == -1) {
            result = db.insert("ingredients", null, values);
        } else {
            result = db.update("ingredients", values, "id = ?",
                    new String[]{String.valueOf(ingredientId)});
            if (result == 0) result = -1; // nothing updated
        }

        if (result != -1) {
            Toast.makeText(this,
                    ingredientId == -1 ? "Ingredient added successfully!"
                                       : "Ingredient updated successfully!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save ingredient.", Toast.LENGTH_SHORT).show();
        }
    }
}
