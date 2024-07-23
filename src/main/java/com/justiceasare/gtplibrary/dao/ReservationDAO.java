package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Reservation;
import com.justiceasare.gtplibrary.model.ReservationType;
import com.justiceasare.gtplibrary.util.DatabaseSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Connection connection;

    public ReservationDAO() {}

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        return DatabaseSource.getConnection();
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.reservation_id, b.title AS book_title, u.username, r.reservation_type, r.reservation_date, r.is_completed " +
                "FROM reservation r " +
                "JOIN book b ON r.book_id = b.book_id " +
                "JOIN user u ON r.user_id = u.user_id";

        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String bookTitle = rs.getString("book_title");
                String username = rs.getString("username");
                ReservationType reservationType = ReservationType.valueOf(rs.getString("reservation_type"));
                LocalDate reservationDate = rs.getDate("reservation_date").toLocalDate();
                boolean completed = rs.getBoolean("is_completed");

                Reservation reservation = new Reservation(reservationId, bookTitle, username, reservationType, reservationDate, completed);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static int addReservation(Reservation reservation) {
        int reservationId = 0;
        String addReservationQuery = "INSERT INTO Reservation (book_id, user_id, reservation_type, reservation_date, is_completed) VALUES (?, ?, ?, ?, ?)";
        String addTransactionQuery = "INSERT INTO TransactionHistory (book_id, user_id, reservation_type, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement addReservationStmt = connection.prepareStatement(addReservationQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement addTransactionStmt = connection.prepareStatement(addTransactionQuery)) {

            Integer bookId = getBookIdByTitle(reservation.getBookTitle());
            Integer userId = getUserIdByUsername(reservation.getUsername());

            if (bookId == null || userId == null) {
                throw new IllegalArgumentException("Book or user cannot be null"); // Book or User not found
            }

            addReservationStmt.setInt(1, bookId);
            addReservationStmt.setInt(2, userId);
            addReservationStmt.setString(3, reservation.getReservationType().name());
            addReservationStmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            addReservationStmt.setBoolean(5, reservation.isCompleted());
            int rowsInserted = addReservationStmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = addReservationStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    reservationId = generatedKeys.getInt(1);
                    reservation.setReservationId(reservationId);

                    // Insert transaction history
                    addTransactionStmt.setInt(1, bookId);
                    addTransactionStmt.setInt(2, userId);
                    addTransactionStmt.setString(3, reservation.getReservationType().name());
                    addTransactionStmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
                    addTransactionStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationId;
    }

    public static boolean updateReservation(Reservation reservation) {
        String updateReservationQuery = "UPDATE Reservation SET book_id = ?, user_id = ?, reservation_type = ?, reservation_date = ?, is_completed = ? WHERE reservation_id = ?";
        String addTransactionQuery = "INSERT INTO TransactionHistory (book_id, user_id, reservation_type, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement updateReservationStmt = connection.prepareStatement(updateReservationQuery);
             PreparedStatement addTransactionStmt = connection.prepareStatement(addTransactionQuery)) {

            Integer bookId = getBookIdByTitle(reservation.getBookTitle());
            Integer userId = getUserIdByUsername(reservation.getUsername());

            if (bookId == null || userId == null) {
                return false; // Book or User not found
            }

            updateReservationStmt.setInt(1, bookId);
            updateReservationStmt.setInt(2, userId);
            updateReservationStmt.setString(3, reservation.getReservationType().name());
            updateReservationStmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            updateReservationStmt.setBoolean(5, reservation.isCompleted());
            updateReservationStmt.setInt(6, reservation.getReservationId());
            int rowsUpdated = updateReservationStmt.executeUpdate();

            if (rowsUpdated > 0) {
                addTransactionStmt.setInt(1, bookId);
                addTransactionStmt.setInt(2, userId);
                addTransactionStmt.setString(3, reservation.getReservationType().name());
                addTransactionStmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
                addTransactionStmt.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String query = "SELECT username FROM user";
        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public static List<String> getAllBookTitles() {
        List<String> bookTitles = new ArrayList<>();
        String query = "SELECT title FROM book b WHERE b.is_archived = false";
        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookTitles.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookTitles;
    }

    public static Integer getBookIdByTitle(String title) {
        String query = "SELECT book_id FROM book WHERE title = ?";
        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("book_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getUserIdByUsername(String username) {
        String query = "SELECT user_id FROM user WHERE username = ?";
        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Reservation getReservationById(int reservationId) {
        String query = "SELECT r.reservation_id, b.title AS book_title, u.username, r.reservation_type, r.reservation_date, r.is_completed " +
                "FROM Reservation r " +
                "JOIN Book b ON r.book_id = b.book_id " +
                "JOIN User u ON r.user_id = u.user_id " +
                "WHERE r.reservation_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Reservation(
                            resultSet.getInt("reservation_id"),
                            resultSet.getString("book_title"),
                            resultSet.getString("username"),
                            ReservationType.valueOf(resultSet.getString("reservation_type")),
                            resultSet.getDate("reservation_date").toLocalDate(),
                            resultSet.getBoolean("is_completed")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if no reservation found with the given ID
    }
}

