package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Fine;
import com.justiceasare.gtplibrary.model.FineUserDTO;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDAO {

    public static String getUserName(int userId) {
        String userName = null;
        String query = "SELECT username FROM user WHERE user_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public static int getUserId(String username) {
        int userId = 0;
        String query = "SELECT user_id FROM user WHERE username = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static ObservableList<String> getAllPatronNames() {
        ObservableList<String> patronNames = FXCollections.observableArrayList();
        String query = "SELECT username FROM user";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                patronNames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patronNames;
    }

    public List<FineUserDTO> getAllFineWithUsername() {
        String query = "select f.fine_id, t.transaction_id, u.username, b.title, f.amount, f.paid from fine f\n" +
                "    left join transaction t ON t.transaction_id = f.transaction_id\n" +
                "    left join user u ON t.user_id = u.user_id\n" +
                "    left join bookcopy bc ON t.copy_id = bc.copy_id\n" +
                "    left join book b ON bc.book_id = b.book_id;";
        List<FineUserDTO> finesByUsername = new ArrayList<>();
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                finesByUsername.add(new FineUserDTO(
                        resultSet.getInt("fine_id"), resultSet.getInt("transaction_id"),
                        resultSet.getString("username"),
                        resultSet.getString("title"),
                        resultSet.getDouble("amount"), resultSet.getBoolean("paid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finesByUsername;
    }

    public static boolean addFine(Fine fine) {
        String query = "INSERT INTO Fine (transaction_id, amount, paid) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, fine.getTransactionId());
            statement.setDouble(2, fine.getAmount());
            statement.setBoolean(3, fine.isPaid());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFine(FineUserDTO fine) {
        String sql = "UPDATE fine SET transaction_id=?, amount=?, paid=? WHERE fine_id=?";
        try (Connection conn =  DatabaseSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, fine.getTransactionId());
            statement.setDouble(2, fine.getAmount());
            statement.setBoolean(3, fine.isPaid());
            statement.setInt(4, fine.getFineId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
