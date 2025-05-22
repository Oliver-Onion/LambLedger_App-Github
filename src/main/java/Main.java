import controller.AppInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		AppInitializer.initializeApplication(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
