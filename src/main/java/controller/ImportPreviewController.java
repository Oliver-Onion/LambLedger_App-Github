package controller;

import dao.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import model.Transaction;
import service.CurrentUserService;
import service.TransactionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.util.*;

public class ImportPreviewController {
    @FXML
    private TableView<Transaction> previewTable;

    private Stage dialogStage;
    private List<Transaction> transactions;
    private Map<String, List<String>> categoryMap;

    @FXML
    public void initialize() {
        loadCategories();
        setupTable();
    }

    private void loadCategories() {
        categoryMap = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/categories.json");
            JsonNode rootNode = mapper.readTree(is);
            JsonNode categoriesNode = rootNode.get("categories");

            for (JsonNode category : categoriesNode) {
                String primaryCategory = category.get("name").asText();
                List<String> secondaryCategories = new ArrayList<>();
                JsonNode children = category.get("children");
                for (JsonNode child : children) {
                    secondaryCategories.add(child.asText());
                }
                categoryMap.put(primaryCategory, secondaryCategories);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        // 获取一级分类和二级分类列
        TableColumn<Transaction, String> primaryCategoryColumn = 
            (TableColumn<Transaction, String>) previewTable.getColumns().get(2);
        TableColumn<Transaction, String> secondaryCategoryColumn = 
            (TableColumn<Transaction, String>) previewTable.getColumns().get(3);

        // 设置表格为可编辑
        previewTable.setEditable(true);

        // 设置一级分类列为可编辑
        primaryCategoryColumn.setCellFactory(column -> {
            ComboBoxTableCell<Transaction, String> cell = new ComboBoxTableCell<>(
                FXCollections.observableArrayList(categoryMap.keySet())
            );
            return cell;
        });

        primaryCategoryColumn.setOnEditCommit(event -> {
            Transaction transaction = event.getRowValue();
            String newValue = event.getNewValue();
            transaction.setPrimaryCategory(newValue);
            
            // 更新二级分类的选项
            List<String> secondaryCategories = categoryMap.get(newValue);
            if (secondaryCategories != null && !secondaryCategories.isEmpty()) {
                transaction.setSecondaryCategory(secondaryCategories.get(0));
            }
            previewTable.refresh();
        });

        // 设置二级分类列为可编辑
        secondaryCategoryColumn.setCellFactory(column -> {
            return new ComboBoxTableCell<Transaction, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        Transaction transaction = getTableRow().getItem();
                        if (transaction != null) {
                            List<String> secondaryCategories = categoryMap.get(transaction.getPrimaryCategory());
                            if (secondaryCategories != null) {
                                getItems().setAll(secondaryCategories);
                            }
                        }
                    }
                }
            };
        });

        secondaryCategoryColumn.setOnEditCommit(event -> {
            Transaction transaction = event.getRowValue();
            transaction.setSecondaryCategory(event.getNewValue());
        });
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        updateTable();
    }

    private void updateTable() {
        previewTable.getItems().clear();
        if (transactions != null) {
            previewTable.getItems().addAll(transactions);
        }
    }

    @FXML
    void confirmImport() {
        List<Transaction> transactions = previewTable.getItems();
        TransactionManager.getInstance().addTransactions(transactions);
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    @FXML
    void cancelImport() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}