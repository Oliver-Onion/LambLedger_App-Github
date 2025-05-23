package model;

public class CategoryBreakdown {
    private String category;
    private double amount;
    private boolean isSubcategory;

    public CategoryBreakdown(String category, double amount, boolean isSubcategory) {
        this.category = category;
        this.amount = amount;
        this.isSubcategory = isSubcategory;
    }

    public String getCategory() {
        return isSubcategory ? "    " + category : category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSubcategory() {
        return isSubcategory;
    }

    public void setSubcategory(boolean subcategory) {
        isSubcategory = subcategory;
    }
} 