package controller;

	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	public static Stage stage;
	public void start(Stage primaryStage) {
		try {
			
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/loginpage.fxml")));
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			stage=primaryStage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
