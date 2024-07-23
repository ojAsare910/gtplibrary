package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.User;
import com.justiceasare.gtplibrary.model.UserType;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private static UserDAO userDAO;
    private static Connection connection;
    private static List<Integer> insertedUserIds;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DatabaseSource.getConnection();
        userDAO = new UserDAO(connection);
    }

    @BeforeEach
    void insertTestUsers() throws SQLException {
        insertedUserIds = new ArrayList<>();

        String insertQuery = "INSERT INTO User (username, email, user_type) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            String[][] testUsers = {
                    {"Sam Larbi", "slarbi@gmail.com", UserType.STAFF.toString()},
                    {"Ntummy Redeemer", "ntummy@gmail.com", UserType.PATRON.toString()},
                    {"Elisha Gyamfi", "elisha@gmail.com", UserType.PATRON.toString()},
            };
            for (String[] testUser : testUsers) {
                statement.setString(1, testUser[0]);
                statement.setString(2, testUser[1]);
                statement.setString(3, testUser[2]);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    insertedUserIds.add(generatedKeys.getInt(1));
                }
            }
        }
    }

    @AfterEach
    void removeTestUsers() throws SQLException {
        String removeQuery = "DELETE FROM User WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(removeQuery)) {
            for (int userId : insertedUserIds) {
                statement.setInt(1, userId);
                statement.executeUpdate();
            }
        }
        insertedUserIds.clear();
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void getAllUsers() {
        List<User> users = userDAO.getAllUsers();

        assertEquals(3, users.size());

        assertEquals("Sam Larbi", users.get(0).getUsername());
        assertEquals("slarbi@gmail.com", users.get(0).getEmail());
        assertEquals(UserType.STAFF, users.get(0).getUserType());
    }

    @Test
    void testAddUserAndRetrieve() throws SQLException {
        User user = new User(0, "Samuel Amo", "samo@gmail.com", UserType.STAFF);

        int userId = userDAO.addUser(user);
        assertTrue(userId > 0);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("Samuel Amo", rs.getString("username"));
            assertEquals("samo@gmail.com", rs.getString("email"));
            assertEquals("STAFF", rs.getString("user_type"));
        }

        String removeQuery = "DELETE FROM User WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(removeQuery)) {
                statement.setInt(1, userId);
                statement.executeUpdate();
        }
    }


    @Test
    void testAddDuplicateUser() throws SQLException {
        User user1 = new User(0, "Michael Nii", "niimichael@gmail.com", UserType.PATRON);
        int user1Id = userDAO.addUser(user1);
        userDAO.addUser(user1);

        User user2 = new User(0, "Michael Nii", "niimichael@gmail.com", UserType.PATRON);
        int result = userDAO.addUser(user2);
        assertEquals(-2, result);

        String removeQuery = "DELETE FROM User WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(removeQuery)) {
            statement.setInt(1, user1Id);
            statement.executeUpdate();
        }
    }

    @Test
    void testUpdateUser() throws SQLException {
        int userId = insertedUserIds.getFirst();
        assertTrue(userId > 0);
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("Sam Larbi", rs.getString("username"));
            assertEquals("slarbi@gmail.com", rs.getString("email"));
            assertEquals("STAFF", rs.getString("user_type"));
        }

        // Now update the user
        User updatedUser = new User(userId, "Agnes Owusu", "oagnes1995@gmail.com", UserType.PATRON);
        boolean result = userDAO.updateUser(updatedUser);
        assertTrue(result);

        // Verify the update
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM User WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("Agnes Owusu", rs.getString("username"));
            assertEquals("oagnes1995@gmail.com", rs.getString("email"));
            assertEquals("PATRON", rs.getString("user_type"));
        }
    }

    @Test
    void testIsValidUser() {
        User validUser = new User(0, "Bernard Adu Gyamfi", "bgyamfi@gmail.com", UserType.PATRON);
        assertTrue(userDAO.isValidUser(validUser));

        // User with username null
        User invalidUser1 = new User(0, "", "validemail@gmail.com", UserType.PATRON);
        assertFalse(userDAO.isValidUser(invalidUser1));

        // User with invalid email address
        User invalidUser2 = new User(0, "Bernard Osei", "obernardymail.com", UserType.PATRON);
        assertFalse(userDAO.isValidUser(invalidUser2));

        // User with null user type
        User invalidUser3 = new User(0, "Bernard Asamoah", "basamoahyahoo.com", null);
        assertFalse(userDAO.isValidUser(invalidUser3));

        // User with all fields as null
        assertFalse(userDAO.isValidUser(null));
    }

    @ParameterizedTest
    @CsvSource({
            "kasare@gmail.com, true",
            "mariamgmail.com, false",
            "'', false",
    })
    void testIsValidEmail(String email, boolean expected) {
        assertEquals(expected, userDAO.isValidEmail(email));
    }

    @Test
    void testIsValidEmailWithNull() {
        assertFalse(userDAO.isValidEmail(null));
    }

    @Test
    void testGetUserById() throws SQLException {
        int userId = insertedUserIds.get(0);
        boolean user = userDAO.userExistsById(userId);
        assertTrue(user);
    }

    @Test
    void testDatabaseConnectionFailure() {
        Connection badConnection = null;
        try {
            badConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/non_existent_db", "user", "password");
        } catch (SQLException e) {
            // Expected exception
        }
        UserDAO badUserDAO = new UserDAO(badConnection);
        List<User> users = badUserDAO.getAllUsers();
        assertFalse(users.isEmpty());
    }

}