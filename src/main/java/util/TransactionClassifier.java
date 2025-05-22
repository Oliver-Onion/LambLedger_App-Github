package util;

import model.Transaction;

public class TransactionClassifier {

    /**
     * 根据交易记录的描述信息，返回对应的分类
     */
    public static String classifyTransaction(Transaction transaction) {
        String description = transaction.getCommodityDescription().toLowerCase();
        return CategoryRuleLoader.getCategory(description);
    }
}