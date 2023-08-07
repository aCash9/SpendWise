package com.example.spendwise;

public class TransactionDetails {
    private int amount;
    private String note;
    private String date;
    private String type;
    private String category;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TransactionDetails() {
    }

    @Override
    public String toString() {
        return "TransactionDetails{" +
                "amount=" + amount +
                ", note='" + note + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public TransactionDetails(int amount, String note, String date, String type, String category) {
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
