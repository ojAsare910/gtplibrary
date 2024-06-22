package com.justiceasare.gtplibrabry.dao;

import com.justiceasare.gtplibrabry.model.Transaction;
import com.justiceasare.gtplibrabry.util.DatabaseSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Transaction> getAllTransactions() {
        String query = "SELECT * FROM Transaction";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transaction_id"), resultSet.getInt("user_id"),
                        resultSet.getInt("copy_id"), resultSet.getString("transaction_type"),
                        resultSet.getTimestamp("transaction_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

}
