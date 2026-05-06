package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DatabaseConnection;
import model.FoodEntry;

public class AddFoodController {

    @FXML
    private TextField foodNameField;

    @FXML
    private TextField caloriesField;

    @FXML
    private TextField proteinField;

    @FXML
    private TextField carbsField;

    @FXML
    private TextField fatsField;

    @FXML
    private ComboBox<String> mealTypeComboBox;

    @FXML
    private DatePicker entryDatePicker;

    @FXML
    private Label messageLabel;

    private final int userId = 1;

    private boolean editMode = false;
    private int editingEntryId;

    @FXML
    public void initialize() {
        mealTypeComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
    }

    public void setFoodEntryToEdit(FoodEntry entry) {
        editMode = true;
        editingEntryId = entry.getEntryId();

        foodNameField.setText(entry.getFoodName());
        caloriesField.setText(String.valueOf(entry.getCalories()));
        proteinField.setText(String.valueOf(entry.getProtein()));
        carbsField.setText(String.valueOf(entry.getCarbs()));
        fatsField.setText(String.valueOf(entry.getFat()));
        mealTypeComboBox.setValue(entry.getMealType());
        entryDatePicker.setValue(LocalDate.parse(entry.getEntryDate()));

        messageLabel.setStyle("-fx-text-fill: blue;");
        messageLabel.setText("Editing food entry.");
    }

    @FXML
    private void handleSaveFood() {
        try {
            String foodName = foodNameField.getText().trim();
            String mealType = mealTypeComboBox.getValue();

            if (foodName.isEmpty()
                    || caloriesField.getText().trim().isEmpty()
                    || proteinField.getText().trim().isEmpty()
                    || carbsField.getText().trim().isEmpty()
                    || fatsField.getText().trim().isEmpty()
                    || mealType == null
                    || entryDatePicker.getValue() == null) {

                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            int calories = Integer.parseInt(caloriesField.getText().trim());
            double protein = Double.parseDouble(proteinField.getText().trim());
            double carbs = Double.parseDouble(carbsField.getText().trim());
            double fats = Double.parseDouble(fatsField.getText().trim());

            if (editMode) {
                updateFood(foodName, calories, protein, carbs, fats, mealType);
            } else {
                insertFood(foodName, calories, protein, carbs, fats, mealType);
            }

        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Error saving food.");
        }
    }

    private void insertFood(String foodName, int calories, double protein,
                            double carbs, double fats, String mealType) throws Exception {

        Connection conn = DatabaseConnection.getConnection();

        String sql = "INSERT INTO food_entries "
                + "(user_id, food_name, calories, protein, carbs, fats, meal_type, entry_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, userId);
        stmt.setString(2, foodName);
        stmt.setInt(3, calories);
        stmt.setDouble(4, protein);
        stmt.setDouble(5, carbs);
        stmt.setDouble(6, fats);
        stmt.setString(7, mealType);
        stmt.setDate(8, java.sql.Date.valueOf(entryDatePicker.getValue()));

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Food added successfully.");

        clearFields();
    }

    private void updateFood(String foodName, int calories, double protein,
                            double carbs, double fats, String mealType) throws Exception {

        Connection conn = DatabaseConnection.getConnection();

        String sql = "UPDATE food_entries "
                + "SET food_name = ?, calories = ?, protein = ?, carbs = ?, fats = ?, meal_type = ?, entry_date = ? "
                + "WHERE entry_id = ? AND user_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, foodName);
        stmt.setInt(2, calories);
        stmt.setDouble(3, protein);
        stmt.setDouble(4, carbs);
        stmt.setDouble(5, fats);
        stmt.setString(6, mealType);
        stmt.setDate(7, java.sql.Date.valueOf(entryDatePicker.getValue()));
        stmt.setInt(8, editingEntryId);
        stmt.setInt(9, userId);

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Food updated successfully.");
    }

    private void clearFields() {
        foodNameField.clear();
        caloriesField.clear();
        proteinField.clear();
        carbsField.clear();
        fatsField.clear();
        mealTypeComboBox.setValue(null);
        entryDatePicker.setValue(null);
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) foodNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Could not return to dashboard.");
        }
    }
}
