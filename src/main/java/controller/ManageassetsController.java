package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.CurrentUserService;
import service.TransactionManager;

public class ManageassetsController implements Initializable {

	@FXML
	private Label netAssetsLabel; // This corresponds to the Label in manageassets.fxml

	public void initialize(URL location, ResourceBundle resources) {
		updateNetAssets();
	}

	private void updateNetAssets() {
		// Get initial money from current user
		double initialMoney = CurrentUserService.getCurrentUser().getMoney();
		
		// Get transaction manager instance and ensure transactions are loaded
		TransactionManager transactionManager = TransactionManager.getInstance();
		transactionManager.loadTransactions();
		
		// Get total income and expenses
		double totalIncome = transactionManager.getAnalyzer().getTotalIncome();
		double totalExpense = transactionManager.getAnalyzer().getTotalExpense();
		
		// Calculate net assets
		double netAssets = initialMoney - totalExpense + totalIncome;
		
		// Format and display net assets
		netAssetsLabel.setText(String.format("￥%.2f", netAssets));
	}

	@FXML
	void learnmore() {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/statistics.fxml")));
			AppInitializer.getPrimaryStage().setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
