package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer {
    private static Stage primaryStage;

    public static void initializeApplication(Stage stage) {
        try {
            primaryStage = stage;
            Scene scene = new Scene(FXMLLoader.load(AppInitializer.class.getResource("/view/loginpage.fxml")));
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
} 