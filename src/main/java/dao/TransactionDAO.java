package dao;

import model.Transaction;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDAO {
    private static final String TRANSACTIONS_FILE = "./resources/transaction_record.csv";

    public void saveNewTransactions(List<Transaction> transactions, int userId) {
        try (FileWriter writer = new FileWriter(TRANSACTIONS_FILE, true)) {
            for (Transaction transaction : transactions) {
                writer.append(String.format("%d,%s,%d,%s,%s,%s,%s,%.2f,%s,%s,%s,%s,%s\n",
                        userId,
                        transaction.getTransactionTime(),
                        transaction.getTransactionDirection(),
                        transaction.getPrimaryCategory(),
                        transaction.getSecondaryCategory(),
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

    public void readTransactions(List<Transaction> transactions, int userId) {
        transactions.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(TRANSACTIONS_FILE), "UTF-8"))) {
            String line;
            // Skip BOM if present
            reader.mark(1);
            if (reader.read() != 0xFEFF) {
                reader.reset();
            }
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 13) {
                    try {
                        // Remove any potential BOM characters and trim whitespace
                        String userIdStr = data[0].trim().replaceAll("\\uFEFF", "");
                        int recordUserId = Integer.parseInt(userIdStr);
                        
                        if (recordUserId == userId) {
                            Transaction transaction = new Transaction();
                            transaction.setTransactionTime(LocalDateTime.parse(data[1].trim()));
                            transaction.setTransactionDirection(Integer.parseInt(data[2].trim()));
                            transaction.setPrimaryCategory(data[3].trim());
                            transaction.setSecondaryCategory(data[4].trim());
                            transaction.setCounterparty(data[5].trim());
                            transaction.setCommodityDescription(data[6].trim());
                            transaction.setAmount(Double.parseDouble(data[7].trim()));
                            transaction.setPaymentMethod(data[8].trim());
                            transaction.setTransactionStatus(data[9].trim());
                            transaction.setTransactionOrderNumber(data[10].trim());
                            transaction.setMerchantOrderNumber(data[11].trim());
                            transaction.setNotes(data[12].trim());

                            // Set transaction direction string based on direction value
                            if (transaction.getTransactionDirection() == 1) {
                                transaction.setTransactionDirectionString("Income");
                            } else if (transaction.getTransactionDirection() == -1) {
                                transaction.setTransactionDirectionString("Expense");
                            } else {
                                transaction.setTransactionDirectionString("Other");
                            }

                            transactions.add(transaction);
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error parsing line: " + line);
                        System.err.println("Error details: " + e.getMessage());
                        continue; // Skip this line and continue with the next one
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
