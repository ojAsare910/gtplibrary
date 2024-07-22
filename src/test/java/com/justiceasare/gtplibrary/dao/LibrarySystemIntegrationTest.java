package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.*;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarySystemIntegrationTest {

    private static Connection connection;
    private static BookDAO bookDAO;
    private static UserDAO userDAO;
    private static ReservationDAO reservationDAO;
    private static TransactionDAO transactionDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DatabaseSource.getConnection();
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO(connection);
        bookDAO = new BookDAO(connection);
        reservationDAO = new ReservationDAO(connection);
        transactionDAO = new TransactionDAO();
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        connection.close();
    }

    @Test
    void testAddBookAndBorrow() throws Exception {
        // Add a new book
        Book book = new Book(0, "Integration Test Book", "Test Author", "1234567890", "Mystery", false);
        int bookId = BookDAO.addBook(book);
        assertTrue(bookId > 0);

        // Register a new user
        User newUser = new User(0, "Samuel Amo", "samo@gmail.com", UserType.STAFF);
        int userId = userDAO.addUser(newUser);
        assertTrue(userId > 0);

        // Borrow a book with the Reservation Service
        Reservation reservation = new Reservation(0, book.getTitle(), newUser.getUsername(), ReservationType.BORROW, LocalDate.now(), false);
        int reservationId = ReservationDAO.addReservation(reservation);
        int transactionId = transactionDAO.getTransactionIdByReservationId(reservationId);
        assertTrue(reservationId > 0);

        // Verify the book is borrowed with the book title
        assertEquals(book.getTitle(), reservationDAO.getAllReservations().getFirst().getBookTitle());

        // Clean up
        String removeQuery = "DELETE FROM TransactionHistory WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(removeQuery)) {
            statement.setInt(1, transactionId);
            statement.executeUpdate();
        }

        String remove4Query = "DELETE FROM Reservation WHERE reservation_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(remove4Query)) {
            statement.setInt(1, reservationId);
            statement.executeUpdate();
        }

        String remove2Query = "DELETE FROM User WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(remove2Query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }

        String remove3Query = "DELETE FROM Book WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(remove3Query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }
}
