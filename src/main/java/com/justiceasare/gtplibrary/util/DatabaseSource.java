package com.justiceasare.gtplibrary.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSource {
    private static final String URL = "jdbc:mysql://localhost:3306/librarymgmt";
    private static final String USER = "libraryadmin";
    private static final String PASSWORD = "admin@1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
