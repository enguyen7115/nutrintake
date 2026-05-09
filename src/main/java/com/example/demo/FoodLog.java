package com.example.demo;

//Logs individual food information
public class FoodLog {

    private String foodName;
    private double servings;
    private double calories;
    private double protein;
    private double carbs;
    private double fiber;
    private double sugar;
    private double fats;
    private double saturated_fat;
    private double trans_fat;
    private double sodium;
    private double cholesterol;

    public FoodLog(String foodName, double servings, double calories, double protein, double carbs, double fiber,
                   double sugar, double fats, double saturated_fat, double trans_fat, double sodium, double cholesterol) {

        this.foodName = foodName;
        this.servings = servings;
        this.calories = calories;
        this.protein = protein;
        this.sugar = sugar;
        this.fiber = fiber;
        this.carbs = carbs;
        this.fats = fats;
        this.saturated_fat = saturated_fat;
        this.trans_fat = trans_fat;
        this.sodium = sodium;
        this.cholesterol = cholesterol;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getServings() {
        return servings;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getSugars() {
        return sugar;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    public double getSodium() {
        return sodium;
    }

    public double getCholesterol() {
        return cholesterol;
    }
}