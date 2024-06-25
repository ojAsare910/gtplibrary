package com.justiceasare.gtplibrary.model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private int bookId;
    private int userId;
    private ReservationType reservationType; // Enum: BORROW or RETURN
    private LocalDate reservationDate;
    private boolean completed;

    public Reservation(int reservationId, int bookId, int userId, ReservationType reservationType, LocalDate reservationDate, boolean completed) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.userId = userId;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.completed = completed;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public ReservationType getReservationType() { return reservationType; }
    public void setReservationType(ReservationType reservationType) { this.reservationType = reservationType; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

