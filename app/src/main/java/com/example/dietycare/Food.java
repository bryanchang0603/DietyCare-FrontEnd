package com.example.dietycare;

public class Food {
    private String foodName;
    private double protein;
    private double fat;
    private double carbo;
    private double calorie;

    public Food(String foodName, double protein, double fat, double carbo, double calorie) {
        this.foodName = foodName;
        this.protein = protein;
        this.fat = fat;
        this.carbo = carbo;
        this.calorie = calorie;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public void setCarbo(Double carbo) {
        this.carbo = carbo;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public String getFoodName() {
        return this.foodName;
    }

    public double getProtein() {
        return this.protein;
    }

    public double getFat() {
        return this.fat;
    }

    public double getCarbo() {
        return this.carbo;
    }

    public double getCalorie() {
        return this.calorie;
    }
}
