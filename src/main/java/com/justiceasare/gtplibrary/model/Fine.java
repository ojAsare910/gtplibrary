package com.justiceasare.gtplibrary.model;

public class Fine {
    private int fineId;
    private int transactionId;
    private double amount;
    private boolean paid;

    public Fine(int fineId, int transactionId, double amount, boolean paid) {
        this.fineId = fineId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paid = paid;
    }

    // Getters and Setters
    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}

