package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class HomeController implements Initializable {

	@FXML
	BorderPane center;
	@FXML
	Button overview;
	@FXML
	Button assets;
	@FXML
	Button habits;
	@FXML
	Button mine;
	@FXML
	Button about;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			center.setCenter(FXMLLoader.load(getClass().getResource("../view/dashboard.fxml")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	void clearBtnStyle(){
		overview.setStyle("-fx-background-color:transparent");
		overview.setText("");
		
		assets.setStyle("-fx-background-color:transparent");
		assets.setText("");
		habits.setStyle("-fx-background-color:transparent");
		habits.setText("");
		mine.setStyle("-fx-background-color:transparent");
		mine.setText("");
		about.setStyle("-fx-background-color:transparent");
		about.setText("");
		
	}
	@FXML
	void overview(){
		clearBtnStyle();
		overview.setText("Overview");
		overview.setStyle("-fx-background-color:#CEDC01; -fx-background-radius: 20;");
		try {
			center.setCenter(FXMLLoader.load(getClass().getResource("../view/dashboard.fxml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	void assets(){
		clearBtnStyle();
		assets.setText("Manage assets");
		assets.setStyle("-fx-background-color:#CEDC01; -fx-background-radius: 20;");
		try {
			center.setCenter(FXMLLoader.load(getClass().getResource("../view/manageassets.fxml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	void habits(){
		clearBtnStyle();
		habits.setText("Spending habits");
		habits.setStyle("-fx-background-color:#CEDC01; -fx-background-radius: 20;");
		try {
			center.setCenter(FXMLLoader.load(getClass().getResource("../view/habits.fxml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	void mine(){
		clearBtnStyle();
		mine.setText("Mine");
		mine.setStyle("-fx-background-color:#CEDC01; -fx-background-radius: 20;");
		try {
			center.setCenter(FXMLLoader.load(getClass().getResource("../view/usercenter2.fxml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	void about(){
		clearBtnStyle();
		about.setText("About Us");
		about.setStyle("-fx-background-color:#CEDC01; -fx-background-radius: 20;");
		try {
			center.setCenter(FXMLLoader.load(getClass().getResource("../view/about2.fxml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
