package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.*;
import com.justiceasare.gtplibrary.util.DatabaseSource;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDAOTest {
    private static Connection connection;
    private static BookDAO bookDAO;
    private static UserDAO userDAO;
    private static ReservationDAO reservationDAO;
    private static TransactionDAO transactionDAO;
    private int userId = 0;
    private int bookId = 0;
    private int reservationId = 0;
    private int transactionId = 0;
    private Book book;
    private Reservation reservation;
    private User user;
    private List<Reservation> reservations;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DatabaseSource.getConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
        userDAO = new UserDAO(connection);
        bookDAO = new BookDAO(connection);
        reservationDAO = new ReservationDAO(connection);
        transactionDAO = new TransactionDAO();

        // Add a new book
        book = new Book(0, "Integration Test Book", "Test Author", "1234567890", "Mystery", false);
        bookId = BookDAO.addBook(book);
        assertTrue(bookId > 0);

        // Register a new user
        user = new User(0, "Samuel Amo", "samo@gmail.com", UserType.STAFF);
        userId = userDAO.addUser(user);
        assertTrue(userId > 0);

    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up
        String[] deleteQueries = {
                "DELETE FROM TransactionHistory WHERE book_id = ? AND user_id = ?",
                "DELETE FROM Reservation WHERE reservation_id = ?",
                "DELETE FROM User WHERE user_id = ?",
                "DELETE FROM Book WHERE book_id = ?"
        };

        // Execute delete queries
        try (PreparedStatement stmt1 = connection.prepareStatement(deleteQueries[0]);
             PreparedStatement stmt2 = connection.prepareStatement(deleteQueries[1]);
             PreparedStatement stmt3 = connection.prepareStatement(deleteQueries[2]);
             PreparedStatement stmt4 = connection.prepareStatement(deleteQueries[3])) {

            // Delete from TransactionHistory
            stmt1.setInt(1, bookId);
            stmt1.setInt(2, userId);
            stmt1.executeUpdate();

            // Delete from Reservation
            stmt2.setInt(1, reservationId);
            stmt2.executeUpdate();

            // Delete from User
            stmt3.setInt(1, userId);
            stmt3.executeUpdate();

            // Delete from Book
            stmt4.setInt(1, bookId);
            stmt4.executeUpdate();
        }
    }

    @Test
    void testGetAllReservations() throws SQLException {
        Reservation reservation = new Reservation(0, book.getTitle(), user.getUsername(), ReservationType.BORROW, LocalDate.now(), false);
        reservationId = ReservationDAO.addReservation(reservation);
        assertTrue(reservationId > 0);
        // Get all reservations
        reservations = reservationDAO.getAllReservations();

        // Assert that the list is not empty
        assertFalse(reservations.isEmpty());

        // Find our test reservation in the list
        Reservation retrievedReservation = reservations.stream()
                .filter(r -> r.getReservationId() == reservationId)
                .findFirst()
                .orElse(null);

        // Assert that our test reservation was found
        assertNotNull(retrievedReservation);
    }

    // Integration Testing
    @Test
    void testAddReservation() throws SQLException {
        // Borrow a book with the Reservation Service
        Reservation reservation = new Reservation(0, book.getTitle(), user.getUsername(), ReservationType.BORROW, LocalDate.now(), false);
        reservationId = ReservationDAO.addReservation(reservation);
        transactionId = transactionDAO.getTransactionIdByReservationId(reservationId);
        assertTrue(reservationId > 0);
        // Verify the book is borrowed with the book title
        assertEquals(book.getTitle(), reservationDAO.getAllReservations().getFirst().getBookTitle());
    }

    @Test
    void testUpdateReservation() throws SQLException {
        // First, add a reservation
        Reservation initialReservation = new Reservation(0, book.getTitle(), user.getUsername(), ReservationType.BORROW, LocalDate.now(), false);
        reservationId = ReservationDAO.addReservation(initialReservation);
        assertTrue(reservationId > 0);

        // Create an updated reservation
        LocalDate newDate = LocalDate.now().plusDays(1);
        Reservation updatedReservation = new Reservation(reservationId, book.getTitle(), user.getUsername(), ReservationType.RETURN, newDate, true);

        // Update the reservation
        boolean updateResult = ReservationDAO.updateReservation(updatedReservation);
        assertTrue(updateResult);

        // Retrieve the updated reservation
        Reservation retrievedReservation = reservationDAO.getReservationById(reservationId);

//         Verify the updates
        assertNotNull(retrievedReservation);
        assertEquals(reservationId, retrievedReservation.getReservationId());
        assertEquals(book.getTitle(), retrievedReservation.getBookTitle());
        assertEquals(user.getUsername(), retrievedReservation.getUsername());
        assertEquals(ReservationType.RETURN, retrievedReservation.getReservationType());
        assertEquals(newDate, retrievedReservation.getReservationDate());
        assertTrue(retrievedReservation.isCompleted());
    }

    @Test
    void testGetAllUsernames() throws SQLException {
        List<String> usernames = ReservationDAO.getAllUsernames();
        assertFalse(usernames.isEmpty());
        assertTrue(usernames.contains(user.getUsername()));
    }

    @Test
    void testGetAllBookTitles() throws SQLException {
        List<String> bookTitles = ReservationDAO.getAllBookTitles();
        assertFalse(bookTitles.isEmpty());
        assertTrue(bookTitles.contains(book.getTitle()));
    }

    @Test
    void testGetBookIdByTitle() throws SQLException {
        Integer retrievedBookId = ReservationDAO.getBookIdByTitle(book.getTitle());
        assertNotNull(retrievedBookId);
        assertEquals(bookId, retrievedBookId.intValue());
    }

    @Test
    void testGetUserIdByUsername() throws SQLException {
        Integer retrievedUserId = ReservationDAO.getUserIdByUsername(user.getUsername());
        assertNotNull(retrievedUserId);
        assertEquals(userId, retrievedUserId.intValue());
    }

    @Test
    void testGetReservationById() throws SQLException {
        // Add a test reservation
        Reservation testReservation = new Reservation(0, book.getTitle(), user.getUsername(), ReservationType.BORROW, LocalDate.now(), false);
        reservationId = ReservationDAO.addReservation(testReservation);
        assertTrue(reservationId > 0);

        // Retrieve the reservation
        Reservation retrievedReservation = reservationDAO.getReservationById(reservationId);

        assertNotNull(retrievedReservation);
        assertEquals(reservationId, retrievedReservation.getReservationId());
        assertEquals(book.getTitle(), retrievedReservation.getBookTitle());
        assertEquals(user.getUsername(), retrievedReservation.getUsername());
        assertEquals(ReservationType.BORROW, retrievedReservation.getReservationType());
        assertEquals(LocalDate.now(), retrievedReservation.getReservationDate());
        assertFalse(retrievedReservation.isCompleted());
    }
}
