package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import model.Transaction;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CategoryRuleLoader {
    private static Map<String, Map<String, String>> categoryRules = new HashMap<>();

    public static void loadCategoryRules() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = CategoryRuleLoader.class.getResourceAsStream("/categories.json");
            JsonNode rootNode = mapper.readTree(is);
            JsonNode categoriesNode = rootNode.get("categories");

            for (JsonNode category : categoriesNode) {
                String primaryCategory = category.get("name").asText();
                categoryRules.putIfAbsent(primaryCategory, new HashMap<>());
                
                JsonNode children = category.get("children");
                for (JsonNode child : children) {
                    String secondaryCategory = child.asText();
                    // Using the secondary category name as the keyword for now
                    // This means if a transaction description contains the exact secondary category name,
                    // it will be classified under that category
                    categoryRules.get(primaryCategory).put(secondaryCategory.toLowerCase(), secondaryCategory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Map<String, String>> getCategoryRules() {
        return categoryRules;
    }
}