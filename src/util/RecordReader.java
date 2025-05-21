package util;

import model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class RecordReader {

    public static Transaction getTransactionByOrderNumber(String csvFilePath, String targetOrderNumber) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // 跳过标题行
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 11) { // 根据CSV列数调整
                    // 提取交易单号
                    String transactionOrderNumber = data[8]; // 根据CSV格式调整索引
                    if (targetOrderNumber.equals(transactionOrderNumber)) {
                        Transaction transaction = new Transaction();
                        transaction.setTransactionTime(LocalDateTime.parse(data[0], formatter));
                        transaction.setTransactionType(data[1]);
                        transaction.setCounterparty(data[2]);
                        transaction.setCommodityDescription(data[3]);
                        transaction.setTransactionDirection(data[4]);
                        transaction.setAmount(Double.parseDouble(data[5]));
                        transaction.setPaymentMethod(data[6]);
                        transaction.setTransactionStatus(data[7]);
                        transaction.setTransactionOrderNumber(data[8]);
                        transaction.setMerchantOrderNumber(data[9]);
                        transaction.setNotes(data[10]);
                        return transaction;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}