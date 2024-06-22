package com.justiceasare.gtplibrabry.model;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int userId;
    private int copyId;
    private String transactionType;
    private Timestamp transactionDate;

    public Transaction(int transactionId, int userId, int copyId, String transactionType, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.copyId = copyId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}

