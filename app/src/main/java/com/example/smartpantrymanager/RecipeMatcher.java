package com.example.smartpantrymanager;

import java.util.List;
import java.util.Locale;

/**
 * Pure-Java strict-matching engine (no Android dependencies, so it is unit-testable).
 * A recipe is feasible only if EVERY ingredient is in the pantry in an equal or greater quantity.
 */
public final class RecipeMatcher {

    private RecipeMatcher() {}

    public static boolean isRecipeFeasible(Recipe recipe, List<Ingredient> pantry) {
        String recipeIngredients = recipe.getIngredients();
        if (recipeIngredients == null || recipeIngredients.trim().isEmpty()) return false;

        for (String ingredient : recipeIngredients.split(",")) {
            String[] details = ingredient.split(":", 3);
            if (details.length != 3) return false;

            String ingredientName = standardizeName(details[0]);
            double requiredQty;
            try {
                requiredQty = Double.parseDouble(details[1].trim());
            } catch (NumberFormatException e) {
                return false;
            }
            String requiredUnit = standardizeUnit(details[2]);
            double requiredBase = toBaseUnitQuantity(requiredQty, requiredUnit);

            double pantrySum = 0;
            for (Ingredient available : pantry) {
                if (available.getName() == null || available.getUnit() == null) continue;
                String availableUnit = standardizeUnit(available.getUnit());
                if (standardizeName(available.getName()).equals(ingredientName)
                        && areUnitsCompatible(availableUnit, requiredUnit)) {
                    pantrySum += toBaseUnitQuantity(available.getQuantity(), availableUnit);
                }
            }

            // small epsilon so 0.1 + 0.2 style floating point sums do not cause false negatives
            if (pantrySum + 1e-9 < requiredBase) return false;
        }
        return true;
    }

    public static String standardizeName(String name) {
        String low = name.toLowerCase(Locale.ROOT).trim();

        if (low.endsWith("oes")) return low.substring(0, low.length() - 2);          // tomatoes, potatoes
        if (low.endsWith("ies")) return low.substring(0, low.length() - 3) + "y";    // berries
        if (low.endsWith("ss") || low.endsWith("us")) return low;                    // hummus, molasses
        if (low.endsWith("s")) return low.substring(0, low.length() - 1);            // eggs, carrots
        return low;
    }

    public static String standardizeUnit(String unit) {
        String u = unit.toLowerCase(Locale.ROOT).trim();
        switch (u) {
            case "item": case "items": case "piece": case "pieces":
                return "items";
            case "g": case "gram": case "grams":
                return "grams";
            case "kg": case "kilogram": case "kilograms":
                return "kg";
            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters":
                return "ml";
            case "l": case "litre": case "litres": case "liter": case "liters":
                return "l";
            default:
                return u;
        }
    }

    public static boolean areUnitsCompatible(String unitA, String unitB) {
        if (unitA.equals("items") && unitB.equals("items")) return true;

        boolean weightA = unitA.equals("grams") || unitA.equals("kg");
        boolean weightB = unitB.equals("grams") || unitB.equals("kg");
        if (weightA && weightB) return true;

        boolean volumeA = unitA.equals("ml") || unitA.equals("l");
        boolean volumeB = unitB.equals("ml") || unitB.equals("l");
        return volumeA && volumeB;
    }

    public static double toBaseUnitQuantity(double quantity, String unit) {
        switch (unit) {
            case "kg":
            case "l":
                return quantity * 1000;
            default:
                return quantity;
        }
    }
}
