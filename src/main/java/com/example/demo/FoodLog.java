package com.example.demo;

//Logs individual food information
public class FoodLog {

    private String foodName;
    private double servings;
    private double calories;
    private double protein;
    private double sugar;
    private double carbs;
    private double fats;
    private double sodium;
    private double cholesterol;

    public FoodLog(String foodName, double servings, double calories, double protein, double sugar,
                   double carbs, double fats, double sodium, double cholesterol) {

        this.foodName = foodName;
        this.servings = servings;
        this.calories = calories;
        this.protein = protein;
        this.sugar = sugar;
        this.carbs = carbs;
        this.fats = fats;
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

    public double getSugar() {
        return sugar;
    }

    public double getCarbs() {return carbs;}

    public double getFats() {return fats;}

    public double getSodium() {return sodium;}

    public double getCholesterol() {return cholesterol;}
}