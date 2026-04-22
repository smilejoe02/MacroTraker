package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

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

    @FXML
    public void initialize() {
        mealTypeComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
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

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Food added successfully.");

            foodNameField.clear();
            caloriesField.clear();
            proteinField.clear();
            carbsField.clear();
            fatsField.clear();
            mealTypeComboBox.setValue(null);
            entryDatePicker.setValue(null);

        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Error saving food.");
        }
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
