package controller;

import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Transaction;
import service.CurrentUserService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImportPreviewController {
    @FXML
    private TableView<Transaction> previewTable;

    private Stage dialogStage;

    private List<Transaction> transactions;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        updateTable();
        setDialogStage(dialogStage);
    }

    private void updateTable() {
        // 更新表格内容
    }

    @FXML
    void confirmImport() {
        // 确认导入
        List<Transaction> transactions = previewTable.getItems();
        int userId = CurrentUserService.getCurrentUser().getId();
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.saveAllTransactions(transactions, userId);
        if (dialogStage != null) { // 先判空
            dialogStage.close(); // 关闭窗口
        }
    }

    @FXML
    void cancelImport() {
        // 取消导入
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
