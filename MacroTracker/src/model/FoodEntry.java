package model;

public class FoodEntry {

    private String foodName;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
    private String entryDate;

    public FoodEntry(String foodName, int calories, double protein, double carbs, double fat, String entryDate) {
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.entryDate = entryDate;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }

    public String getEntryDate() {
        return entryDate;
    }
}