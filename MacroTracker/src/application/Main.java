package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.DatabaseConnection;
import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //database connection
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("MacroTracker database is ready!");
        } else {
            System.out.println("Check your database settings.");
        }

        //login screen
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("MacroTracker Login");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
