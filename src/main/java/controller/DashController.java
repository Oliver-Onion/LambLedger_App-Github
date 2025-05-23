package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.TransactionManager;

public class DashController implements Initializable {

	@FXML
	private Label totalIncomeLabel;
	
	@FXML
	private Label totalExpenseLabel;

	public void initialize(URL location, ResourceBundle resources) {
		// Get the transaction manager instance
		TransactionManager transactionManager = TransactionManager.getInstance();
		
		// Load transactions if not already loaded
		transactionManager.loadTransactions();
		
		// Get the analyzer from transaction manager
		double totalIncome = transactionManager.getAnalyzer().getTotalIncome();
		double totalExpense = transactionManager.getAnalyzer().getTotalExpense();
		
		// Update labels with formatted values
		totalIncomeLabel.setText(String.format("%.2f", totalIncome));
		totalExpenseLabel.setText(String.format("%.2f", totalExpense));
	}
	@FXML
	void learnmore(){
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/statistics.fxml")));
			AppInitializer.getPrimaryStage().setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
