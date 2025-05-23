package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CategoryBreakdown;
import service.TransactionManager;
import util.TransactionAnalyzer;

public class DashController implements Initializable {

	@FXML
	private Label totalIncomeLabel;
	
	@FXML
	private Label totalExpenseLabel;

	@FXML
	private TableView<CategoryBreakdown> incomeTable;
	
	@FXML
	private TableColumn<CategoryBreakdown, String> incomeCategoryCol;
	
	@FXML
	private TableColumn<CategoryBreakdown, Double> incomeAmountCol;
	
	@FXML
	private TableView<CategoryBreakdown> expenditureTable;
	
	@FXML
	private TableColumn<CategoryBreakdown, String> expenditureCategoryCol;
	
	@FXML
	private TableColumn<CategoryBreakdown, Double> expenditureAmountCol;

	public void initialize(URL location, ResourceBundle resources) {
		// Get the transaction manager instance
		TransactionManager transactionManager = TransactionManager.getInstance();
		
		// Load transactions if not already loaded
		transactionManager.loadTransactions();
		
		// Get the analyzer from transaction manager
		double totalIncome = transactionManager.getAnalyzer().getTotalIncome();
		double totalExpense = transactionManager.getAnalyzer().getTotalExpense();
		
		// Update labels with formatted values
		totalIncomeLabel.setText(String.format("￥%.2f", totalIncome));
		totalExpenseLabel.setText(String.format("￥%.2f", totalExpense));

		// Set up table columns
		incomeCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		incomeAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		
		expenditureCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		expenditureAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

		// Load data into tables
		loadCategoriesFromJson();
	}

	private void loadCategoriesFromJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = getClass().getResourceAsStream("/categories.json");
			if (is == null) {
				System.err.println("Could not find categories.json in resources");
				return;
			}

			JsonNode rootNode = mapper.readTree(is);
			JsonNode categoriesNode = rootNode.get("categories");

			List<CategoryBreakdown> incomeBreakdowns = new ArrayList<>();
			List<CategoryBreakdown> expenseBreakdowns = new ArrayList<>();

			TransactionManager transactionManager = TransactionManager.getInstance();
			TransactionAnalyzer analyzer = transactionManager.getAnalyzer();

			for (JsonNode category : categoriesNode) {
				String primaryCategory = category.get("name").asText();
				String type = category.get("type").asText();
				double primaryAmount = analyzer.getPrimaryCategoryAmount(primaryCategory);

				CategoryBreakdown primaryBreakdown = new CategoryBreakdown(
					primaryCategory, 
					primaryAmount,
					false
				);

				if ("income".equals(type)) {
					incomeBreakdowns.add(primaryBreakdown);
				} else if ("expense".equals(type)) {
					expenseBreakdowns.add(primaryBreakdown);
				}

				// Add subcategories
				JsonNode children = category.get("children");
				for (JsonNode child : children) {
					String secondaryCategory = child.asText();
					double secondaryAmount = analyzer.getSecondaryCategoryAmount(secondaryCategory);

					CategoryBreakdown secondaryBreakdown = new CategoryBreakdown(
						secondaryCategory,
						secondaryAmount,
						true
					);

					if ("income".equals(type)) {
						incomeBreakdowns.add(secondaryBreakdown);
					} else if ("expense".equals(type)) {
						expenseBreakdowns.add(secondaryBreakdown);
					}
				}
			}

			incomeTable.setItems(FXCollections.observableArrayList(incomeBreakdowns));
			expenditureTable.setItems(FXCollections.observableArrayList(expenseBreakdowns));

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error reading categories.json: " + e.getMessage());
		}
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
