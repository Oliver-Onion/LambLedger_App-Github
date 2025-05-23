package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import model.Transaction;
import service.TransactionManager;

public class HabitsController implements Initializable {

	@FXML
	ListView<String> list1;
	@FXML
	ListView<String> list2;
	@FXML
	ListView<String> list3;
	@FXML
	TextArea aiResponseArea;
	@FXML
	Button suggestBudgetButton;
	@FXML
	Button suggestSavingsButton;
	@FXML
	Button suggestReductionButton;

	private static final String API_KEY = "sk-c8e7556c86dc42a69907fdce8cd690a6";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static JsonNode prompts;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	ObservableList<String> items = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load prompts
		try {
			String promptsJson = new String(Files.readAllBytes(Paths.get("resources/prompts.json")));
			prompts = objectMapper.readTree(promptsJson);
		} catch (IOException e) {
			System.err.println("Failed to load prompts: " + e.getMessage());
		}

		// Initialize lists
		for (int i = 0; i < 4; i++) {
			items.add("note"+i);
		}
		list1.setItems(items);
		list2.setItems(items);
		list3.setItems(items);
		
		// Set transparent background for list cells
		ListView<String>[] lists = new ListView[]{list1, list2, list3};
		for (ListView<String> list : lists) {
			list.setCellFactory(param -> new ListCell<String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
						setStyle("-fx-background-color: transparent;");
					} else {
						setText(item);
						setStyle("-fx-background-color: transparent;");
					}
				}
			});
		}
	}

	private String getTransactionContext() {
		List<Transaction> transactions = TransactionManager.getInstance().getTransactions();
		StringBuilder context = new StringBuilder();
		for (Transaction t : transactions) {
			context.append(String.format("%s %s %s %.2f\n", 
				t.getTransactionTime().format(DATE_FORMATTER),
				t.getCounterparty(),
				t.getCommodityDescription(),
				t.getAmount()));
		}
		return context.toString();
	}

	private void handleAIRequest(String promptType) {
		try {
			JsonNode promptNode = prompts.get(promptType);
			if (promptNode == null) {
				throw new IllegalArgumentException("Invalid prompt type: " + promptType);
			}

			Generation gen = new Generation();
			
			// System message
			Message systemMsg = Message.builder()
					.role(Role.SYSTEM.getValue())
					.content(promptNode.get("system").asText())
					.build();
			
			// User message with transaction context
			Message userMsg = Message.builder()
					.role(Role.USER.getValue())
					.content(promptNode.get("user").asText() + "\n" + getTransactionContext())
					.build();

			GenerationParam param = GenerationParam.builder()
					.apiKey(API_KEY)
					.model("qwen-turbo")
					.messages(Arrays.asList(systemMsg, userMsg))
					.resultFormat(GenerationParam.ResultFormat.MESSAGE)
					.build();

			GenerationResult result = gen.call(param);
			String response = result.getOutput().getChoices().get(0).getMessage().getContent();
			
			aiResponseArea.setText(response);
			
		} catch (Exception e) {
			aiResponseArea.setText("Failed to connect to AI service: " + e.getMessage());
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("AI Service Error");
			alert.setHeaderText(null);
			alert.setContentText("Failed to connect to AI service. Please try again later.");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleSuggestBudget() {
		handleAIRequest("monthly_budget");
	}

	@FXML
	private void handleSuggestSavings() {
		handleAIRequest("saving_goal");
	}

	@FXML
	private void handleSuggestReduction() {
		handleAIRequest("expense_advice");
	}
}
