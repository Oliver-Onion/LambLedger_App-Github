package dao;

import model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TransactionDAO {
    private static final String TRANSACTIONS_FILE = "./resources/transaction_record.csv";
    public boolean importTransaction(Transaction transaction, int userId) {
        return true;
    }
    public void saveAllTransactions(List<Transaction> transactions, int userId) {
        try (FileWriter writer = new FileWriter(TRANSACTIONS_FILE, true)) {
            for (Transaction transaction : transactions) {
                writer.append(String.format("%d,%s,%s,%s,%s,%.2f,%s,%s,%s,%s,%s,%d\n",
                        userId,
                        transaction.getTransactionTime(),
                        transaction.getTransactionType(),
                        transaction.getCounterparty(),
                        transaction.getCommodityDescription(),
                        transaction.getAmount(),
                        transaction.getPaymentMethod(),
                        transaction.getTransactionStatus(),
                        transaction.getTransactionOrderNumber(),
                        transaction.getMerchantOrderNumber(),
                        transaction.getNotes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
