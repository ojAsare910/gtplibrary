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
        String query = "SELECT th.transaction_id, th.book_id, th.user_id, th.reservation_type, th.transaction_date,\n" +
                "       b.title AS book_title, u.username AS username\n" +
                "FROM transactionhistory th\n" +
                "         JOIN book b ON th.book_id = b.book_id\n" +
                "         JOIN user u ON th.user_id = u.user_id\n" +
                "ORDER BY th.transaction_id;";

        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int bookId = rs.getInt("book_id");
                int userId = rs.getInt("user_id");
                ReservationType reservationType = ReservationType.valueOf(rs.getString("reservation_type"));
                LocalDate transactionDate = rs.getDate("transaction_date").toLocalDate();
                String bookTitle = rs.getString("book_title");
                String username = rs.getString("username");

                TransactionHistory transaction = new TransactionHistory(transactionId, bookTitle, username, reservationType, transactionDate);
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }


    public int getTransactionIdByReservationId(int reservationId) {
        int transactionId = 0;
        String query = "SELECT th.transaction_id\n" +
                "    FROM transactionhistory th\n" +
                "    JOIN book b ON th.book_id = b.book_id\n" +
                "    JOIN user u ON th.user_id = u.user_id\n" +
                "    JOIN Reservation r on b.book_id = R.book_id\n" +
                "    WHERE r.reservation_id = ? ORDER BY th.transaction_id;";

        try (Connection conn = DatabaseSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
             stmt.setInt(1, reservationId);
             try (ResultSet rs = stmt.executeQuery()) {
                 if (rs.next()) {
                     transactionId = rs.getInt("transaction_id");
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionId;
    }
}
