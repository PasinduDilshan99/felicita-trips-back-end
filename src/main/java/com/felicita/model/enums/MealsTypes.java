package com.felicita.model.enums;

public enum MealsTypes {

    NONE("No Meals"),
    BREAKFAST("Breakfast"),
    HALF_BOARD("Half Board (Breakfast + Dinner)"),
    FULL_BOARD("Full Board (All Meals)"),
    ALL_INCLUSIVE("All Inclusive"),
    BREAKFAST_ONLY("Breakfast Only"),
    LUNCH_ONLY("Lunch Only"),
    DINNER_ONLY("Dinner Only"),
    VEGAN("Vegan Meals"),
    VEGETARIAN("Vegetarian Meals"),
    NON_VEGETARIAN("Non-Vegetarian Meals");

    private final String displayName;

    MealsTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}