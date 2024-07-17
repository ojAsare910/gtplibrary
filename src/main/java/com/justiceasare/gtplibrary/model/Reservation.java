package com.justiceasare.gtplibrary.model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private String bookTitle;
    private String username;
    private ReservationType reservationType;
    private LocalDate reservationDate;
    private boolean completed;

    // Constructor with reservationId
    public Reservation(int reservationId, String bookTitle, String username, ReservationType reservationType, LocalDate reservationDate, boolean completed) {
        this.reservationId = reservationId;
        this.bookTitle = bookTitle;
        this.username = username;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.completed = completed;
    }

    // Constructor without reservationId
    public Reservation(String bookTitle, String username, ReservationType reservationType, LocalDate reservationDate, boolean completed) {
        this.bookTitle = bookTitle;
        this.username = username;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.completed = completed;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}

