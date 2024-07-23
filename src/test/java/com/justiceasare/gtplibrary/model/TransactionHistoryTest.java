package com.justiceasare.gtplibrary.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionHistoryTest {

    private TransactionHistory transactionHistory;

    @BeforeEach
    void setUp() {
        transactionHistory = new TransactionHistory(1, "Effective Java", "john_doe", ReservationType.BORROW, LocalDate.now());
    }

    @AfterEach
    void tearDown() {
        transactionHistory = null;
    }

    @Test
    void testGetTransactionId() {
        assertEquals(1, transactionHistory.getTransactionId());
    }

    @Test
    void testSetTransactionId() {
        transactionHistory.setTransactionId(2);
        assertEquals(2, transactionHistory.getTransactionId());
    }

    @Test
    void testGetBookTitle() {
        assertEquals("Effective Java", transactionHistory.getBookTitle());
    }

    @Test
    void testSetBookTitle() {
        transactionHistory.setBookTitle("Java Concurrency in Practice");
        assertEquals("Java Concurrency in Practice", transactionHistory.getBookTitle());
    }

    @Test
    void testGetUsername() {
        assertEquals("john_doe", transactionHistory.getUsername());
    }

    @Test
    void testSetUsername() {
        transactionHistory.setUsername("jane_doe");
        assertEquals("jane_doe", transactionHistory.getUsername());
    }

    @Test
    void testGetReservationType() {
        assertEquals(ReservationType.BORROW, transactionHistory.getReservationType());
    }

    @Test
    void testSetReservationType() {
        transactionHistory.setReservationType(ReservationType.RETURN);
        assertEquals(ReservationType.RETURN, transactionHistory.getReservationType());
    }

    @Test
    void testGetTransactionDate() {
        LocalDate now = LocalDate.now();
        transactionHistory.setTransactionDate(now);
        assertEquals(now, transactionHistory.getTransactionDate());
    }
}