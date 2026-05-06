package model;

public class FoodEntry {

    private int entryId;
    private String foodName;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
    private String mealType;
    private String entryDate;

    public FoodEntry(int entryId, String foodName, int calories, double protein,
                     double carbs, double fat, String mealType, String entryDate) {
        this.entryId = entryId;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.mealType = mealType;
        this.entryDate = entryDate;
    }

    public int getEntryId() {
        return entryId;
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

    public String getMealType() {
        return mealType;
    }

    public String getEntryDate() {
        return entryDate;
    }
}