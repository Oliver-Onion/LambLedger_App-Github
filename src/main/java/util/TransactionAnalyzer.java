package util;

import model.Transaction;
import java.util.*;

public class TransactionAnalyzer {
    // 存储一级分类的收支统计
    private Map<String, CategoryStats> primaryCategoryStats;
    // 存储二级分类的收支统计
    private Map<String, CategoryStats> secondaryCategoryStats;
    // 存储当前分析的交易列表
    private List<Transaction> currentTransactions;

    public TransactionAnalyzer() {
        this.primaryCategoryStats = new HashMap<>();
        this.secondaryCategoryStats = new HashMap<>();
        this.currentTransactions = new ArrayList<>();
    }

    // 内部类用于存储每个分类的收支统计
    public static class CategoryStats {
        private double amount;

        public CategoryStats() {
            this.amount = 0.0;
        }

        public void addAmount(double amount) {
            this.amount += amount;
        }

        public double getAmount() {
            return amount;
        }
    }

    /**
     * 根据一级分类名称获取该分类的统计金额
     * @param categoryName 一级分类名称
     * @return 该分类的总金额
     */
    public double getPrimaryCategoryAmount(String categoryName) {
        CategoryStats stats = primaryCategoryStats.get(categoryName);
        return stats != null ? stats.getAmount() : 0.0;
    }

    /**
     * 根据二级分类名称获取该分类的统计金额
     * @param categoryName 二级分类名称
     * @return 该分类的总金额
     */
    public double getSecondaryCategoryAmount(String categoryName) {
        CategoryStats stats = secondaryCategoryStats.get(categoryName);
        return stats != null ? stats.getAmount() : 0.0;
    }

    /**
     * 更新所有统计数据
     */
    public void updateStatistics(List<Transaction> transactions) {
        // 清空之前的统计数据
        primaryCategoryStats.clear();
        secondaryCategoryStats.clear();
        
        // 保存当前交易列表的引用
        this.currentTransactions = new ArrayList<>(transactions);

        // 重新分析所有交易
        analyzeTransactions(transactions);
    }

    /**
     * 分析交易列表并统计各类别的收支情况
     */
    private void analyzeTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            String primaryCategory = transaction.getPrimaryCategory();
            String secondaryCategory = transaction.getSecondaryCategory();
            double amount = transaction.getAmount();

            // 更新一级分类统计
            primaryCategoryStats.computeIfAbsent(primaryCategory, k -> new CategoryStats())
                    .addAmount(amount);

            // 更新二级分类统计
            secondaryCategoryStats.computeIfAbsent(secondaryCategory, k -> new CategoryStats())
                    .addAmount(amount);
        }
    }

    /**
     * 获取一级分类的统计结果
     */
    public Map<String, CategoryStats> getPrimaryCategoryStats() {
        return primaryCategoryStats;
    }

    /**
     * 获取二级分类的统计结果
     */
    public Map<String, CategoryStats> getSecondaryCategoryStats() {
        return secondaryCategoryStats;
    }

    /**
     * 打印统计结果
     */
    public void printAnalysis() {
        System.out.println("\n=== 一级分类统计 ===");
        primaryCategoryStats.forEach((category, stats) -> {
            System.out.printf("%s: %.2f\n", category, stats.getAmount());
        });

        System.out.println("\n=== 二级分类统计 ===");
        secondaryCategoryStats.forEach((category, stats) -> {
            System.out.printf("%s: %.2f\n", category, stats.getAmount());
        });
    }

    /**
     * 获取总收入
     */
    public double getTotalIncome() {
        return currentTransactions.stream()
                .filter(transaction -> transaction.getTransactionDirection() == 1)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * 获取总支出
     */
    public double getTotalExpense() {
        return currentTransactions.stream()
                .filter(transaction -> transaction.getTransactionDirection() == -1)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * 获取净资产（考虑交易方向）
     */
    public double getNetAsset() {
        return currentTransactions.stream()
                .mapToDouble(transaction -> 
                    transaction.getAmount() * transaction.getTransactionDirection())
                .sum();
    }
} 