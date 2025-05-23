package util;

import model.Transaction;
import java.util.Map;
import java.util.Arrays;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TransactionClassifier {
    private static final String API_KEY = "sk-c8e7556c86dc42a69907fdce8cd690a6";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String systemPrompt;
    private static String userPrompt;

    static {
        try {
            // 读取prompts.json
            String promptsJson = new String(Files.readAllBytes(Paths.get("resources/prompts.json")));
            JsonNode prompts = objectMapper.readTree(promptsJson);
            
            // 读取categories.json
            String categoriesJson = new String(Files.readAllBytes(Paths.get("resources/categories.json")));
            
            // 获取基础提示词
            systemPrompt = prompts.get("classification").get("system").asText();
            userPrompt = prompts.get("classification").get("user").asText();
            
            // 将categories.json的内容添加到system prompt中
            systemPrompt = systemPrompt + " " + categoriesJson;
            
        } catch (IOException e) {
            System.err.println("Failed to load prompts or categories: " + e.getMessage());
            // 使用默认提示词作为后备
            systemPrompt = "你是专业的交易分类助手，需根据用户提供的交易记录返回一级分类和二级分类。返回格式必须为JSON：{\"primaryCategory\": \"类别\", \"secondaryCategory\": \"子类别\"}";
            userPrompt = "请为以下交易描述分类：";
        }
    }

    /**
     * 输出交易分类结果
     */
    private static void printClassificationResult(Transaction transaction, String method) {
        System.out.println("Method: " + method);
        System.out.println("Details:" + transaction.getCounterparty() + " " + transaction.getCommodityDescription() + " " + transaction.getTransactionDirection());
        System.out.println("Classification Result:" + transaction.getPrimaryCategory() + " " + transaction.getSecondaryCategory());
    }

    /**
     * 设置默认分类
     */
    private static void setDefaultCategory(Transaction transaction) {
        String direction = transaction.getTransactionDirection();
        if (direction != null && direction.contains("收")) {
            transaction.setPrimaryCategory("Other Incomes");
            transaction.setSecondaryCategory("Other Incomes");
        } else {
            transaction.setPrimaryCategory("Other Expenses");
            transaction.setSecondaryCategory("Other Expenses");
        }
    }

    /**
     * 从文本中提取JSON字符串
     */
    private static String extractJsonFromText(String text) {
        // 移除所有的反引号和换行符
        text = text.replace("`", "").replace("\n", " ").trim();
        
        // 使用正则表达式匹配JSON对象
        Pattern pattern = Pattern.compile("\\{[^}]*\\}");
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            return matcher.group();
        }
        
        throw new IllegalArgumentException("No valid JSON found in response");
    }

    /**
     * 使用Qwen API对交易进行智能分类
     */
    private static boolean classifyUsingQwenAPI(Transaction transaction) {
        try {
            Generation gen = new Generation();
            
            // System message with prompts from config
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content(systemPrompt)
                    .build();
            
            // User message with transaction description
            String transactionInfo = transaction.getCounterparty() + " " +
                                   transaction.getCommodityDescription() + " " +
                                   transaction.getTransactionDirection();

            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(userPrompt + transactionInfo)
                    .build();

            GenerationParam param = GenerationParam.builder()
                    .apiKey(API_KEY)
                    .model("qwen-turbo")
                    .messages(Arrays.asList(systemMsg, userMsg))
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            // 清理并解析JSON响应
            String jsonStr = extractJsonFromText(content);
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            
            // 验证JSON结构
            if (!jsonNode.has("primaryCategory") || !jsonNode.has("secondaryCategory")) {
                throw new IllegalArgumentException("Invalid JSON structure: missing required fields");
            }
            
            String primaryCategory = jsonNode.get("primaryCategory").asText();
            String secondaryCategory = jsonNode.get("secondaryCategory").asText();
            
            // 验证分类不为空
            if (primaryCategory.isEmpty() || secondaryCategory.isEmpty()) {
                throw new IllegalArgumentException("Categories cannot be empty");
            }
            
            transaction.setPrimaryCategory(primaryCategory);
            transaction.setSecondaryCategory(secondaryCategory);
            
            // 输出AI分类结果
            printClassificationResult(transaction, "AI Classification (Qwen)");
            
            return true;
        } catch (Exception e) {
            // Log the error but continue with rule-based classification
            System.err.println("Failed to classify using Qwen API: " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据交易记录的描述信息，返回对应的分类
     */
    public static void classifyTransaction(Transaction transaction) {
        // First try to classify using Qwen API
        if (API_KEY != null && !API_KEY.isEmpty()) {
            if (classifyUsingQwenAPI(transaction)) {
                return;
            }
        }

        // Fallback to rule-based classification
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
                    // 输出规则分类结果
                    printClassificationResult(transaction, "Rule-based Classification");
                    return;
                }
            }
        }
        // 根据交易方向设置默认分类
        setDefaultCategory(transaction);
        // 输出默认分类结果
        printClassificationResult(transaction, "Default Classification");
    }
}