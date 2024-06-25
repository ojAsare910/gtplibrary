package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Reservation;
import com.justiceasare.gtplibrary.model.ReservationType;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation";

        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                int bookId = rs.getInt("book_id");
                int userId = rs.getInt("user_id");
                ReservationType reservationType = ReservationType.valueOf(rs.getString("reservation_type"));
                LocalDate reservationDate = rs.getDate("reservation_date").toLocalDate();
                boolean completed = rs.getBoolean("is_completed");

                Reservation reservation = new Reservation(reservationId, bookId, userId, reservationType, reservationDate, completed);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static boolean addReservation(Reservation reservation) {
        String addReservationQuery = "INSERT INTO Reservation (book_id, user_id, reservation_type, reservation_date, is_completed) VALUES (?, ?, ?, ?, ?)";
        String addTransactionQuery = "INSERT INTO TransactionHistory (book_id, user_id, reservation_type, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement addReservationStmt = connection.prepareStatement(addReservationQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement addTransactionStmt = connection.prepareStatement(addTransactionQuery)) {

            addReservationStmt.setInt(1, reservation.getBookId());
            addReservationStmt.setInt(2, reservation.getUserId());
            addReservationStmt.setString(3, reservation.getReservationType().name());
            addReservationStmt.setDate(4, Date.valueOf(reservation.getReservationDate())); // Convert LocalDate to SQL Date
            addReservationStmt.setBoolean(5, reservation.isCompleted());
            int rowsInserted = addReservationStmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = addReservationStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int reservationId = generatedKeys.getInt(1);
                    reservation.setReservationId(reservationId);

                    // Insert transaction history
                    addTransactionStmt.setInt(1, reservation.getBookId());
                    addTransactionStmt.setInt(2, reservation.getUserId());
                    addTransactionStmt.setString(3, reservation.getReservationType().name());
                    addTransactionStmt.setDate(4, Date.valueOf(reservation.getReservationDate())); // Convert LocalDate to SQL Date
                    addTransactionStmt.executeUpdate();

                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReservation(Reservation reservation) {
        String updateReservationQuery = "UPDATE Reservation SET book_id = ?, user_id = ?, reservation_type = ?, reservation_date = ?, is_completed = ? WHERE reservation_id = ?";
        String addTransactionQuery = "INSERT INTO TransactionHistory (book_id, user_id, reservation_type, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement updateReservationStmt = connection.prepareStatement(updateReservationQuery);
             PreparedStatement addTransactionStmt = connection.prepareStatement(addTransactionQuery)) {

            updateReservationStmt.setInt(1, reservation.getBookId());
            updateReservationStmt.setInt(2, reservation.getUserId());
            updateReservationStmt.setString(3, reservation.getReservationType().name());
            updateReservationStmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            updateReservationStmt.setBoolean(5, reservation.isCompleted());
            updateReservationStmt.setInt(6, reservation.getReservationId());
            int rowsUpdated = updateReservationStmt.executeUpdate();

            if (rowsUpdated > 0) {
                addTransactionStmt.setInt(1, reservation.getBookId());
                addTransactionStmt.setInt(2, reservation.getUserId());
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

    public boolean existsPatron(int patronId) {
        String query = "SELECT COUNT(*) FROM patron WHERE user_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

