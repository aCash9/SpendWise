package com.example.spendwise;

public class categoryDetails {
    private String categoryName;
    private int categoryAmount;

    public categoryDetails(String categoryName, int categoryAmount) {
        this.categoryName = categoryName;
        this.categoryAmount = categoryAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryAmount() {
        return categoryAmount;
    }

    public void setCategoryAmount(int categoryAmount) {
        this.categoryAmount = categoryAmount;
    }
}
