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
    private List<Transaction> transactions = new ArrayList<>();

    public WechatPayImporter(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public boolean importWechatPayCSV(String filePath, int userId) {
        transactions.clear(); // 清空之前的记录
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // 跳过标题行
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 11) {
                    Transaction transaction = new Transaction();
                    // 修改日期时间格式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/dd HH:mm");
                    try {
                        // 增加空值检查
                        if (data[0] != null &&!data[0].trim().isEmpty()) {
                            transaction.setTransactionTime(LocalDateTime.parse(data[0], formatter));
                        }
                    } catch (Exception e) {
                        System.err.println("日期解析错误: " + data[0] + "，错误信息: " + e.getMessage());
                        continue;
                    }
                    try {
                        transaction.setTransactionType(data[1]);
                        transaction.setCounterparty(data[2]);
                        transaction.setCommodityDescription(data[3]);
                        transaction.setTransactionDirection(data[4]);
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

                        transactions.add(transaction);
                        transactionDAO.importTransaction(transaction, userId);
                    } catch (Exception e) {
                        System.err.println("数据解析错误: " + line + "，错误信息: " + e.getMessage());
                    }
                }
            }
            System.out.println("\n=== 导入的交易记录 ===");
            System.out.println("序号\t时间\t\t\t类型\t对方\t\t金额\t分类");
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                System.out.printf("%d\t%s\t%s\t%s\t%.2f\t%s%n",
                        i + 1,
                        t.getTransactionTime(),
                        t.getTransactionType(),
                        t.getCounterparty(),
                        t.getAmount(),
                        t.getPrimaryCategory()
                );
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}