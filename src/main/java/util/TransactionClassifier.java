package util;

import model.Transaction;
import java.util.Map;

public class TransactionClassifier {

    /**
     * 根据交易记录的描述信息，返回对应的分类
     */
    public static void classifyTransaction(Transaction transaction) {
        String description = transaction.getCommodityDescription().toLowerCase();
        Map<String, Map<String, String>> categoryRules = CategoryRuleLoader.getCategoryRules();
        
        for (Map.Entry<String, Map<String, String>> primaryEntry : categoryRules.entrySet()) {
            String primaryCategory = primaryEntry.getKey();
            Map<String, String> secondaryCategories = primaryEntry.getValue();
            
            for (Map.Entry<String, String> secondaryEntry : secondaryCategories.entrySet()) {
                String keyword = secondaryEntry.getKey();
                if (description.contains(keyword)) {
                    transaction.setPrimaryCategory(primaryCategory);
                    transaction.setSecondaryCategory(secondaryEntry.getValue());
                    return;
                }
            }
        }
        // If no category matches, set as "其他"
        transaction.setPrimaryCategory("其他");
        transaction.setSecondaryCategory("其他");
    }
}