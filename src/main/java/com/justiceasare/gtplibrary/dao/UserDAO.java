package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.User;
import com.justiceasare.gtplibrary.model.UserType;
import com.justiceasare.gtplibrary.util.DatabaseSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserDAO {

    private Connection connection;
    public UserDAO() {}
    public UserDAO(Connection connection) {
        this.connection = connection;
    }
    protected Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        return DatabaseSource.getConnection();
    }

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

    // Adds a new user to the database
    public int addUser(User user) {
        if (!isValidUser(user)) {
            return -1; // Invalid user data
        }

        if (userExists(user.getUsername(), user.getEmail())) {
            return -2; // User already exists
        }

        String query = "INSERT INTO User (username, email, user_type) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUserType().name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return -1; // Insert failed
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the generated userId
            } else {
                return -1; // Insert failed
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Insert failed
        }
    }

    // Updates an existing user in the database
    public boolean updateUser(User user) {
        if (!isValidUser(user) || user.getUserId() <= 0) {
            return false; // Invalid user data
        }

        if (!userExistsById(user.getUserId())) {
            return false; // User does not exist
        }

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

    // Checks if a user's data is valid
    public boolean isValidUser(User user) {
        if (user == null) {
            return false;
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            return false;
        }
        if (user.getUserType() == null) {
            return false;
        }
        return true;
    }

    // Validates an email address format
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    // Checks if a user with the given username or email already exists
    public boolean userExists(String username, String email) {
        String query = "SELECT COUNT(*) FROM User WHERE username = ? OR email = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Checks if a user with the given user ID exists
    public boolean userExistsById(int userId) {
        String query = "SELECT COUNT(*) FROM User WHERE user_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
