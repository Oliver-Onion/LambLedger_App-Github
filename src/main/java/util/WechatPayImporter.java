package util;

import dao.TransactionDAO;
import model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WechatPayImporter {
    private TransactionDAO transactionDAO;
    private List<Transaction> newTransactions = new ArrayList<>();

    public WechatPayImporter(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    // 添加辅助方法来格式化日期字符串
    private String padDateTimeString(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return dateStr;
        }
        
        // 分割日期和时间
        String[] parts = dateStr.split("\\s+");
        if (parts.length != 2) {
            return dateStr;
        }

        // 处理日期部分
        String[] dateParts = parts[0].split("/");
        if (dateParts.length != 3) {
            return dateStr;
        }

        // 补充年月日
        String year = dateParts[0];
        String month = dateParts[1].length() == 1 ? "0" + dateParts[1] : dateParts[1];
        String day = dateParts[2].length() == 1 ? "0" + dateParts[2] : dateParts[2];

        // 处理时间部分
        String[] timeParts = parts[1].split(":");
        if (timeParts.length != 2) {
            return dateStr;
        }

        // 补充时分
        String hour = timeParts[0].length() == 1 ? "0" + timeParts[0] : timeParts[0];
        String minute = timeParts[1].length() == 1 ? "0" + timeParts[1] : timeParts[1];

        // 返回格式化后的字符串
        return String.format("%s/%s/%s %s:%s", year, month, day, hour, minute);
    }

    public boolean importWechatPayCSV(String filePath, int userId) {
        newTransactions.clear(); // 清空之前的记录
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // 跳过标题行
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 11) {
                    Transaction transaction = new Transaction();
                    // 修改日期时间格式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    try {
                        // 增加空值检查并格式化日期字符串
                        if (data[0] != null && !data[0].trim().isEmpty()) {
                            String formattedDateTime = padDateTimeString(data[0]);
                            transaction.setTransactionTime(LocalDateTime.parse(formattedDateTime, formatter));
                        }
                    } catch (Exception e) {
                        System.err.println("日期解析错误: " + data[0] + "，错误信息: " + e.getMessage());
                        continue;
                    }
                    try {
                        transaction.setTransactionType(data[1]);
                        transaction.setCounterparty(data[2]);
                        transaction.setCommodityDescription(data[3]);
                        // 根据收/支字段设置对应的整数值
                        String direction = data[4];
                        if (direction.contains("收")) {
                            transaction.setTransactionDirection(1);
                            transaction.setTransactionDirectionString("Income");
                        } else if (direction.contains("支")) {
                            transaction.setTransactionDirection(-1);
                            transaction.setTransactionDirectionString("Expense");
                        } else {
                            transaction.setTransactionDirection(0);
                            transaction.setTransactionDirectionString("Other");
                        }
                        // 增加数字解析的异常处理，去除人民币符号
                        try {
                            String amountStr = data[5].replace("¥", "");
                            transaction.setAmount(Double.parseDouble(amountStr));
                        } catch (NumberFormatException e) {
                            System.err.println("金额解析错误: " + data[5] + "，错误信息: " + e.getMessage());
                            continue;
                        }
                        transaction.setPaymentMethod(data[6]);
                        transaction.setTransactionStatus(data[7]);
                        transaction.setTransactionOrderNumber(data[8]);
                        transaction.setMerchantOrderNumber(data[9]);
                        transaction.setNotes(data[10]);

                        // 调用 classifyTransaction 方法对交易进行分类
                        TransactionClassifier.classifyTransaction(transaction);

                        newTransactions.add(transaction);
                    } catch (Exception e) {
                        System.err.println("数据解析错误: " + line + "，错误信息: " + e.getMessage());
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getNewTransactions() {
        return newTransactions;
    }
}