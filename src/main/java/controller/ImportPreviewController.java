package controller;

import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Transaction;
import service.CurrentUserService;
import service.TransactionManager;

import java.util.List;

public class ImportPreviewController {
    @FXML
    private TableView<Transaction> previewTable;

    private Stage dialogStage;

    private List<Transaction> transactions;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        updateTable();
    }

    private void updateTable() {
        previewTable.getItems().clear();  // 清空现有数据
        if (transactions != null) {
            previewTable.getItems().addAll(transactions);  // 添加新数据
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