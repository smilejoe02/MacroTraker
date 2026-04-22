package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private static final String ALLOWED_USER = "joe";
    private static final String ALLOWED_PASS = "1234";

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.equals(ALLOWED_USER) && password.equals(ALLOWED_PASS)) {
            messageLabel.setText("Login successful!");
            openDashboard();
        } else {
            messageLabel.setText("Login unsuccessful.");
            passwordField.clear();
        }
    }

    private void openDashboard() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            stage.setTitle("MacroTracker Dashboard");
            stage.setScene(scene);
        } catch (IOException e) {
            messageLabel.setText("Could not open dashboard.");
            e.printStackTrace();
        }
    }
}