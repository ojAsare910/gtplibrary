package com.justiceasare.gtplibrary.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation(1, "Effective Java", "john_doe", ReservationType.BORROW, LocalDate.now(), false);
    }

    @AfterEach
    void tearDown() {
        reservation = null;
    }

    @Test
    void testGetReservationId() {
        assertEquals(1, reservation.getReservationId());
    }

    @Test
    void testSetReservationId() {
        reservation.setReservationId(2);
        assertEquals(2, reservation.getReservationId());
    }

    @Test
    void testGetBookTitle() {
        assertEquals("Effective Java", reservation.getBookTitle());
    }

    @Test
    void testSetBookTitle() {
        reservation.setBookTitle("Java Concurrency in Practice");
        assertEquals("Java Concurrency in Practice", reservation.getBookTitle());
    }

    @Test
    void testGetUsername() {
        assertEquals("john_doe", reservation.getUsername());
    }

    @Test
    void testSetUsername() {
        reservation.setUsername("jane_doe");
        assertEquals("jane_doe", reservation.getUsername());
    }

    @Test
    void testGetReservationType() {
        assertEquals(ReservationType.BORROW, reservation.getReservationType());
    }

    @Test
    void testSetReservationType() {
        reservation.setReservationType(ReservationType.RETURN);
        assertEquals(ReservationType.RETURN, reservation.getReservationType());
    }

    @Test
    void testGetReservationDate() {
        LocalDate now = LocalDate.now();
        reservation.setReservationDate(now);
        assertEquals(now, reservation.getReservationDate());
    }

    @Test
    void testIsCompleted() {
        assertFalse(reservation.isCompleted());
    }

    @Test
    void testSetCompleted() {
        reservation.setCompleted(true);
        assertTrue(reservation.isCompleted());
    }
}
