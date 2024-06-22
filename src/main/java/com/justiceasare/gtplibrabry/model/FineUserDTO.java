package com.justiceasare.gtplibrabry.model;

public class FineUserDTO {
    private int fineId;
    private int transactionId;
    private String username;
    private String title;
    private double amount;
    private boolean paid;

    public FineUserDTO(int fineId, int transactionId, String username, String title, double amount, boolean paid) {
        this.fineId = fineId;
        this.transactionId = transactionId;
        this.username = username;
        this.title = title;
        this.amount = amount;
        this.paid = paid;
    }

    public int getFineId() {
        return fineId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTitle() {
        return this.title;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
