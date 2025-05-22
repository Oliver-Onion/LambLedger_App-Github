package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CategoryRuleLoader {
    private static Map<String, Map<String, String>> categoryRules = new HashMap<>();

    public static void loadCategoryRules() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        CategoryRuleLoader.class.getResourceAsStream("/csv/category_rules.csv")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String primaryCategory = parts[0];
                    String secondaryCategory = parts[1];
                    String[] keywords = new String[parts.length - 2];
                    for (int i = 2; i < parts.length; i++) {
                        keywords[i - 2] = parts[i];
                    }

                    categoryRules.putIfAbsent(primaryCategory, new HashMap<>());
                    for (String keyword : keywords) {
                        categoryRules.get(primaryCategory).put(keyword.toLowerCase(), secondaryCategory);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCategory(String keyword) {
        if (categoryRules.isEmpty()) {
            loadCategoryRules();
        }

        for (Map.Entry<String, Map<String, String>> entry : categoryRules.entrySet()) {
            String primaryCategory = entry.getKey();
            Map<String, String> secondaryCategories = entry.getValue();
            for (Map.Entry<String, String> secondaryEntry : secondaryCategories.entrySet()) {
                String ruleKeyword = secondaryEntry.getKey();
                String secondaryCategory = secondaryEntry.getValue();
                if (keyword.toLowerCase().contains(ruleKeyword)) {
                    return primaryCategory + " - " + secondaryCategory;
                }
            }
        }
        return "其他 - 其他";
    }
}