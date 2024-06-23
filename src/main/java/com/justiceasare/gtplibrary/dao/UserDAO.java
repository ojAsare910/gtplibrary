package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.User;
import com.justiceasare.gtplibrary.model.UserType;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> getAllUsers() {
        String query = "SELECT * FROM User";
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("user_id"), resultSet.getString("username"),
                        resultSet.getString("email"), UserType.valueOf(resultSet.getString("user_type"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean addUser(User user) {
        String query = "INSERT INTO User (username, email, user_type) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUserType().name());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE User SET username = ?, email = ?, user_type = ? WHERE user_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUserType().name());
            statement.setInt(4, user.getUserId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
