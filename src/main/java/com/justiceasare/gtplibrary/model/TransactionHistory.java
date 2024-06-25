package com.justiceasare.gtplibrary.model;

import java.time.LocalDate;

public class TransactionHistory {
    private int transactionId;
    private int bookId;
    private int userId;
    private ReservationType reservationType; // Enum: BORROW or RETURN
    private LocalDate transactionDate;

    public TransactionHistory(int transactionId, int bookId, int userId, ReservationType reservationType, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.reservationType = reservationType;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public ReservationType getReservationType() { return reservationType; }
    public void setReservationType(ReservationType reservationType) { this.reservationType = reservationType; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
}

