package com.justiceasare.gtplibrary.model;

import java.time.LocalDate;

public class TransactionHistory {
    private int transactionId;
    private int bookId;
    private int userId;
    private String bookTitle;
    private String username;
    private ReservationType reservationType;
    private LocalDate transactionDate;

    // Constructor, getters, and setters
    public TransactionHistory(int transactionId, String bookTitle, String username, ReservationType reservationType, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.bookTitle = bookTitle;
        this.username = username;
        this.reservationType = reservationType;
        this.transactionDate = transactionDate;
    }

    // Other getters and setters

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationType reservationType) {
        this.reservationType = reservationType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
