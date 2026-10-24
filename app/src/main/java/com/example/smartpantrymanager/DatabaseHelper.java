package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

    private static DatabaseHelper instance;

    /** One shared helper for the whole app (avoids multiple open connections / closing a shared DB). */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create ingredients table
        String createIngredientsTable = "CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiryDate TEXT)";

        db.execSQL(createIngredientsTable);


        // Create recipes table
        String createRecipesTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "ingredients TEXT NOT NULL, " +
                "instructions TEXT)";

        db.execSQL(createRecipesTable);


        // Add the default recipes
        insertDefaultRecipes(db);
    }

    private void insertDefaultRecipes(SQLiteDatabase db) {

        // 1. Tomato Omelette
        insertRecipe(
                db,
                "Tomato Omelette",
                "A simple omelette made with eggs, tomatoes, onion and cheese.",
                "egg:2:items,tomato:2:items,onion:1:items,cheese:30:grams",
                "Beat the eggs. Chop the tomato and onion. Cook the vegetables in a pan, add the eggs, sprinkle with cheese, and cook until set."
        );

        // 2. Scrambled Eggs
        insertRecipe(
                db,
                "Scrambled Eggs",
                "Simple creamy scrambled eggs.",
                "egg:3:items,milk:30:ml,butter:10:grams",
                "Crack the eggs into a bowl and mix with milk. Melt butter in a pan, add the eggs, and stir gently until cooked."
        );

        // 3. Cheese Toast
        insertRecipe(
                db,
                "Cheese Toast",
                "Crispy toast filled with melted cheese.",
                "bread:2:items,cheese:50:grams,butter:10:grams",
                "Butter the bread and add cheese. Toast in a pan or toaster until the bread is golden and the cheese has melted."
        );

        // 4. Tomato Sandwich
        insertRecipe(
                db,
                "Tomato Sandwich",
                "A quick sandwich made with fresh tomatoes and cheese.",
                "bread:2:items,tomato:1:items,cheese:30:grams,butter:10:grams",
                "Slice the tomato. Butter the bread, add tomato and cheese, then close the sandwich and toast if desired."
        );

        // 5. Pasta with Tomato Sauce
        insertRecipe(
                db,
                "Pasta with Tomato Sauce",
                "Pasta served with a simple tomato and garlic sauce.",
                "pasta:200:grams,tomato:3:items,onion:1:items,garlic:2:items,olive oil:20:ml",
                "Cook the pasta. Chop the tomatoes, onion and garlic. Cook them in olive oil until soft, then mix the sauce with the pasta."
        );

        // 6. Egg Fried Rice
        insertRecipe(
                db,
                "Egg Fried Rice",
                "Fried rice with egg and vegetables.",
                "rice:200:grams,egg:2:items,onion:1:items,carrot:1:items,olive oil:20:ml",
                "Cook the rice. Fry the onion and carrot in oil, add the eggs and scramble them, then add the cooked rice and stir-fry."
        );

        // 7. Chicken Fried Rice
        insertRecipe(
                db,
                "Chicken Fried Rice",
                "Fried rice with chicken, egg and vegetables.",
                "rice:200:grams,chicken:150:grams,egg:1:items,onion:1:items,carrot:1:items,olive oil:20:ml",
                "Cook the rice. Cook the chicken in a pan, then add onion and carrot. Add the egg and cooked rice and stir-fry everything together."
        );

        // 8. Chicken Pasta
        insertRecipe(
                db,
                "Chicken Pasta",
                "Simple pasta with cooked chicken, onion and garlic.",
                "pasta:200:grams,chicken:150:grams,onion:1:items,garlic:2:items,olive oil:20:ml",
                "Cook the pasta. Cut and cook the chicken. Fry onion and garlic, add the chicken and pasta, and mix together."
        );

        // 9. Banana Pancakes
        insertRecipe(
                db,
                "Banana Pancakes",
                "Soft pancakes made with banana, eggs and milk.",
                "banana:1:items,egg:2:items,flour:100:grams,milk:100:ml",
                "Mash the banana and mix with eggs, flour and milk. Pour small amounts into a hot pan and cook both sides until golden."
        );

        // 10. French Toast
        insertRecipe(
                db,
                "French Toast",
                "Golden bread cooked with an egg and milk mixture.",
                "bread:2:items,egg:2:items,milk:50:ml,butter:10:grams",
                "Beat the eggs and milk together. Dip the bread into the mixture, then fry in butter until golden on both sides."
        );

        // 11. Vegetable Rice
        insertRecipe(
                db,
                "Vegetable Rice",
                "Rice mixed with simple fried vegetables.",
                "rice:200:grams,carrot:1:items,onion:1:items,tomato:1:items,olive oil:20:ml",
                "Cook the rice. Chop the vegetables and fry them in olive oil. Add the cooked rice and mix together."
        );

        // 12. Tuna Sandwich
        insertRecipe(
                db,
                "Tuna Sandwich",
                "A quick tuna sandwich with tomato and mayonnaise.",
                "bread:2:items,tuna:1:items,mayonnaise:20:ml,tomato:1:items",
                "Mix the tuna with mayonnaise. Slice the tomato. Add the tuna mixture and tomato to the bread and serve."
        );

        // 13. Chicken Sandwich
        insertRecipe(
                db,
                "Chicken Sandwich",
                "A simple chicken sandwich with tomato and cheese.",
                "bread:2:items,chicken:100:grams,tomato:1:items,cheese:30:grams,mayonnaise:20:ml",
                "Cook and slice the chicken. Slice the tomato. Add chicken, tomato, cheese and mayonnaise to the bread."
        );

        // 14. Tomato Pasta
        insertRecipe(
                db,
                "Tomato Pasta",
                "Pasta with a simple tomato, garlic and cheese sauce.",
                "pasta:200:grams,tomato:3:items,garlic:2:items,olive oil:20:ml,cheese:30:grams",
                "Cook the pasta. Cook the tomatoes and garlic in olive oil to make a sauce. Mix with the pasta and top with cheese."
        );

        // 15. Banana French Toast
        insertRecipe(
                db,
                "Banana French Toast",
                "French toast topped with fresh banana.",
                "bread:2:items,banana:1:items,egg:2:items,milk:50:ml,butter:10:grams",
                "Mash the banana and mix it with eggs and milk. Dip the bread into the mixture and fry in butter until golden."
        );
    }

    private void insertRecipe(
            SQLiteDatabase db,
            String name,
            String description,
            String ingredients,
            String instructions
    ) {

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("description", description);
        values.put("ingredients", ingredients);
        values.put("instructions", instructions);

        db.insert("recipes", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Never wipe the user's pantry on upgrade. Only the seeded recipes are refreshed.
        db.execSQL("CREATE TABLE IF NOT EXISTS ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiryDate TEXT)");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "ingredients TEXT NOT NULL, " +
                "instructions TEXT)");
        insertDefaultRecipes(db);
    }
}
