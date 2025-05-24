package com;

import javafx.scene.chart.PieChart;
import service.TransactionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.control.Tooltip;

public class PieDashboardExpense extends PieChart {

	public PieDashboardExpense() {
		// Get transaction manager instance and ensure data is loaded
		TransactionManager transactionManager = TransactionManager.getInstance();
		transactionManager.loadTransactions();

		try {
			// Load categories from JSON
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = getClass().getResourceAsStream("/categories.json");
			if (is == null) {
				System.err.println("Could not find categories.json in resources");
				return;
			}

			JsonNode rootNode = mapper.readTree(is);
			JsonNode categoriesNode = rootNode.get("categories");

			// Calculate total expense for percentage
			double totalExpense = 0;
			List<PieChart.Data> pieData = new ArrayList<>();
			
			// First pass: calculate total and create data objects
			for (JsonNode category : categoriesNode) {
				if ("expense".equals(category.get("type").asText())) {
					String categoryName = category.get("name").asText();
					double amount = Math.abs(transactionManager.getAnalyzer().getPrimaryCategoryAmount(categoryName));
					if (amount > 0) {
						totalExpense += amount;
						pieData.add(new PieChart.Data(categoryName, amount));
					}
				}
			}

			// If total expense is 0, hide the chart
			if (totalExpense == 0) {
				this.setVisible(false);
				return;
			}

			// Add all pie chart data
			this.getData().addAll(pieData);

			// Configure pie chart display
			this.setLegendSide(Side.RIGHT);
			this.setLabelsVisible(false);
			
			// Add tooltips with percentage information
			for (PieChart.Data data : this.getData()) {
				double percentage = (data.getPieValue() / totalExpense) * 100;
				Tooltip tooltip = new Tooltip(String.format("%s\n￥%.2f (%.1f%%)", 
					data.getName(), 
					data.getPieValue(),
					percentage));
				Tooltip.install(data.getNode(), tooltip);
				
				// Add mouse event handlers for interactive effects
				data.getNode().setOnMouseEntered(e -> {
					data.getNode().setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-scale-x: 1.1; -fx-scale-y: 1.1;");
				});
				
				data.getNode().setOnMouseExited(e -> {
					data.getNode().setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
				});
			}

			// Style the pie chart
			this.setStyle("-fx-background-color: white; -fx-padding: 20;");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading expense categories: " + e.getMessage());
		}
	}
}
