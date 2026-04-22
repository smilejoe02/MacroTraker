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

            String sql = "SELECT food_name, calories, protein, carbs, fats, entry_date "
                    + "FROM food_entries WHERE user_id = ? ORDER BY entry_date DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FoodEntry entry = new FoodEntry(
                        rs.getString("food_name"),
                        rs.getInt("calories"),
                        rs.getDouble("protein"),
                        rs.getDouble("carbs"),
                        rs.getDouble("fats"),
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
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("MacroTracker Dashboard");
        stage.setMaximized(true);
        stage.show();
    }
}