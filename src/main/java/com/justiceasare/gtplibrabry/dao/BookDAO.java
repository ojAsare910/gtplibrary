package com.justiceasare.gtplibrabry.dao;

import com.justiceasare.gtplibrabry.model.Book;
import com.justiceasare.gtplibrabry.util.DatabaseSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooks() {
        String query = "SELECT * FROM Book";
        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt("book_id"), resultSet.getString("title"), resultSet.getString("category")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean addBook(Book book) {
        String query = "INSERT INTO Book (title, category) VALUES (?, ?)";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getCategory());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateBook(Book book) {
        String query = "UPDATE Book SET title = ?, category = ? WHERE book_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getCategory());
            statement.setInt(3, book.getBookId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBook(int bookId) {
        String query = "DELETE FROM Book WHERE book_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
