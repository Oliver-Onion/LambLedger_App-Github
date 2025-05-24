package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BillCenterController implements Initializable {

	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	void backToStatistics() {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/statistics.fxml")));
			AppInitializer.getPrimaryStage().setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
