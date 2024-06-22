package com.justiceasare.gtplibrabry.dao;

import com.justiceasare.gtplibrabry.model.Reservation;
import com.justiceasare.gtplibrabry.util.DatabaseSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public List<Reservation> getAllReservations() {
        String query = "SELECT * FROM Reservation";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reservations.add(new Reservation(resultSet.getInt("reservation_id"), resultSet.getInt("patron_id"),
                        resultSet.getInt("book_id"), resultSet.getTimestamp("reservation_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public Reservation getReservationById(int reservationId) {
        String query = "SELECT * FROM Reservation WHERE reservation_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservationId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Reservation(resultSet.getInt("reservation_id"), resultSet.getInt("patron_id"),
                        resultSet.getInt("book_id"), resultSet.getTimestamp("reservation_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addReservation(Reservation reservation) {
        String query = "INSERT INTO Reservation (patron_id, book_id) VALUES (?, ?)";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservation.getPatronId());
            statement.setInt(2, reservation.getBookId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE Reservation SET patron_id = ?, book_id = ?, reservation_date = ? WHERE reservation_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservation.getPatronId());
            statement.setInt(2, reservation.getBookId());
            statement.setTimestamp(3, reservation.getReservationDate());
            statement.setInt(4, reservation.getReservationId());
            return statement.executeUpdate() > 0;
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

