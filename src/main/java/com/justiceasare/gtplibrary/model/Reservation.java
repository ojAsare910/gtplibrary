package com.justiceasare.gtplibrary.model;

import java.sql.Timestamp;

public class Reservation {
    private int reservationId;
    private int patronId;
    private int bookId;
    private Timestamp reservationDate;

    public Reservation(int reservationId, int patronId, int bookId, Timestamp reservationDate) {
        this.reservationId = reservationId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }
}

