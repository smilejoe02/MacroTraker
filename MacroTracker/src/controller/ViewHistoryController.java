package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DatabaseConnection;
import model.FoodEntry;

public class ViewHistoryController {

    @FXML
    private TableView<FoodEntry> historyTable;

    @FXML
    private TableColumn<FoodEntry, String> foodNameColumn;

    @FXML
    private TableColumn<FoodEntry, Integer> caloriesColumn;

    @FXML
    private TableColumn<FoodEntry, Double> proteinColumn;

    @FXML
    private TableColumn<FoodEntry, Double> carbsColumn;

    @FXML
    private TableColumn<FoodEntry, Double> fatColumn;

    @FXML
    private TableColumn<FoodEntry, String> dateColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        foodNameColumn.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        fatColumn.setCellValueFactory(new PropertyValueFactory<>("fat"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("entryDate"));

        loadHistory();
    }

    private void loadHistory() {
        ObservableList<FoodEntry> foodList = FXCollections.observableArrayList();

        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "SELECT entry_id, food_name, calories, protein, carbs, fats, meal_type, entry_date "
                    + "FROM food_entries WHERE user_id = ? ORDER BY entry_date DESC, entry_id DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FoodEntry entry = new FoodEntry(
                        rs.getInt("entry_id"),
                        rs.getString("food_name"),
                        rs.getInt("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
                        rs.getString("meal_type"),
                        rs.getString("entry_date")
                );

                foodList.add(entry);
            }

            historyTable.setItems(foodList);

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditSelected() {
        FoodEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();

        if (selectedEntry == null) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Please select a food entry to edit.");
            }
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addfood.fxml"));
            Parent root = loader.load();

            AddFoodController controller = loader.getController();
            controller.setFoodEntryToEdit(selectedEntry);

            Stage stage = (Stage) historyTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Food Entry");
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();

            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Could not open edit page.");
            }
        }
    }

    @FXML
    private void handleDeleteSelected() {
        FoodEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();

        if (selectedEntry == null) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Please select a food entry to delete.");
            }
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "DELETE FROM food_entries WHERE entry_id = ? AND user_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selectedEntry.getEntryId());
            stmt.setInt(2, 1);

            stmt.executeUpdate();

            stmt.close();
            conn.close();

            loadHistory();

            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Food entry deleted successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Error deleting food entry.");
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadHistory();

        if (messageLabel != null) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("History refreshed.");
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("MacroTracker Dashboard");
        stage.setMaximized(true);
        stage.show();
    }
}