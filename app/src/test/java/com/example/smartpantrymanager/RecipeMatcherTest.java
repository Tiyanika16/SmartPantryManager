package com.example.smartpantrymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeMatcherTest {

    private static Recipe recipe(String ingredients) {
        return new Recipe(1, "Test", "desc", ingredients, "steps");
    }

    private static Ingredient ing(String name, double qty, String unit) {
        return new Ingredient(1, name, qty, unit, null);
    }

    @Test
    public void allIngredientsPresent_isFeasible() {
        List<Ingredient> pantry = Arrays.asList(
                ing("Eggs", 3, "items"), ing("Milk", 100, "ml"), ing("Butter", 20, "grams"));
        assertTrue(RecipeMatcher.isRecipeFeasible(recipe("egg:3:items,milk:30:ml,butter:10:grams"), pantry));
    }

    @Test
    public void missingOneIngredient_isNotFeasible() {
        List<Ingredient> pantry = Arrays.asList(ing("Eggs", 3, "items"), ing("Milk", 100, "ml"));
        assertFalse(RecipeMatcher.isRecipeFeasible(recipe("egg:3:items,milk:30:ml,butter:10:grams"), pantry));
    }

    @Test
    public void insufficientQuantity_isNotFeasible() {
        assertFalse(RecipeMatcher.isRecipeFeasible(recipe("egg:3:items"), Arrays.asList(ing("egg", 2, "items"))));
    }

    @Test
    public void unitsAreConverted() {
        assertTrue(RecipeMatcher.isRecipeFeasible(recipe("milk:500:ml"), Arrays.asList(ing("milk", 1, "litres"))));
        assertTrue(RecipeMatcher.isRecipeFeasible(recipe("rice:200:grams"), Arrays.asList(ing("rice", 1, "kg"))));
        assertFalse(RecipeMatcher.isRecipeFeasible(recipe("milk:500:ml"), Arrays.asList(ing("milk", 400, "grams"))));
    }

    @Test
    public void duplicateEntriesAreSummed() {
        assertTrue(RecipeMatcher.isRecipeFeasible(recipe("egg:3:items"),
                Arrays.asList(ing("egg", 2, "items"), ing("Eggs", 1, "items"))));
    }

    @Test
    public void emptyPantryOrBadData_isNotFeasible() {
        assertFalse(RecipeMatcher.isRecipeFeasible(recipe("egg:1:items"), new ArrayList<>()));
        assertFalse(RecipeMatcher.isRecipeFeasible(recipe("garbage"), new ArrayList<>()));
    }

    @Test
    public void namesAreNormalised() {
        assertEquals("tomato", RecipeMatcher.standardizeName("Tomatoes"));
        assertEquals("potato", RecipeMatcher.standardizeName("potatoes"));
        assertEquals("cherry", RecipeMatcher.standardizeName("cherries"));
        assertEquals("egg", RecipeMatcher.standardizeName(" EGGS "));
        assertEquals("hummus", RecipeMatcher.standardizeName("hummus"));
    }
}
