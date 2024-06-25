package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.ReservationType;
import com.justiceasare.gtplibrary.model.TransactionHistory;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<TransactionHistory> getAllTransactions() {
        List<TransactionHistory> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactionhistory";

        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int bookId = rs.getInt("book_id");
                int userId = rs.getInt("user_id");
                ReservationType reservationType = ReservationType.valueOf(rs.getString("reservation_type"));
                LocalDate transactionDate = rs.getDate("transaction_date").toLocalDate();

                TransactionHistory transaction = new TransactionHistory(transactionId, bookId, userId, reservationType, transactionDate);
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public boolean addTransaction(TransactionHistory transaction) {
        String sql = "INSERT INTO TransactionHistory (book_id, user_id, reservation_type, transaction_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getBookId());
            stmt.setInt(2, transaction.getUserId());
            stmt.setString(3, transaction.getReservationType().name());
            stmt.setDate(4, java.sql.Date.valueOf(transaction.getTransactionDate()));

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
