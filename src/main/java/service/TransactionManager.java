package service;

import model.Transaction;
import dao.TransactionDAO;
import util.TransactionAnalyzer;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static TransactionManager instance;
    private List<Transaction> transactions;
    private TransactionDAO transactionDAO;
    private TransactionAnalyzer analyzer;

    private TransactionManager() {
        transactions = new ArrayList<>();
        transactionDAO = new TransactionDAO();
        analyzer = new TransactionAnalyzer();
    }

    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    public void loadTransactions() {
        int userId = CurrentUserService.getCurrentUser().getId();
        transactions.clear();
        transactionDAO.readTransactions(transactions, userId);
        analyzer.updateStatistics(transactions);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransactions(List<Transaction> newTransactions) {
        int userId = CurrentUserService.getCurrentUser().getId();
        transactionDAO.saveNewTransactions(newTransactions, userId);
        transactions.addAll(newTransactions);
        analyzer.updateStatistics(transactions);
    }

    public TransactionAnalyzer getAnalyzer() {
        return analyzer;
    }

    public void clearTransactions() {
        transactions.clear();
        analyzer.updateStatistics(transactions);
    }
} 