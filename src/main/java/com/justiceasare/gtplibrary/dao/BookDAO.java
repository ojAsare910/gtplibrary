package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Book;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDAO {

    private Connection connection;

    public BookDAO() {}

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        return DatabaseSource.getConnection();
    }

    public static List<Book> getAllBooks() {
        String query = "SELECT b.book_id, b.title, b.author, b.isbn, b.category" +
                " FROM Book b WHERE is_archived = 0";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            Map<Integer, Book> bookMap = new HashMap<>();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                Book book = bookMap.get(bookId);

                if (book == null) {
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String isbn = resultSet.getString("isbn");
                    String category = resultSet.getString("category");
                    book = new Book(bookId, title, author, isbn, category, false);
                    bookMap.put(bookId, book);
                }
            }

            books.addAll(bookMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Check if a book with the given title and ISBN exists
    public static boolean bookExists(String title, String isbn) {
        String query = "SELECT COUNT(*) FROM Book WHERE title = ? AND isbn = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, isbn);
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

    // Add a new book with validation to check if the book already exists
    public static int addBook(Book book) throws IllegalArgumentException {
        String insertBookSQL = "INSERT INTO Book (title, author, isbn, category, is_archived) VALUES (?, ?, ?, ?, ?)";

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        int bookId = -1;

        // Check if the book already exists
        if (bookExists(book.getTitle(), book.getIsbn())) {
            return bookId; // Return -1 indicating failure to add
        }

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement bookStatement = connection.prepareStatement(insertBookSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insert the book into the Book table
            bookStatement.setString(1, book.getTitle());
            bookStatement.setString(2, book.getAuthor());
            bookStatement.setString(3, book.getIsbn());
            bookStatement.setString(4, book.getCategory());
            bookStatement.setBoolean(5, book.getIs_archived());
            int rowsInserted = bookStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated book ID
                try (ResultSet generatedKeys = bookStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bookId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookId;
    }

    // Update a book with validation to check if the book exists
    public static boolean updateBook(Book book) {
        String query = "UPDATE Book SET title = ?, author = ?, isbn = ?, category = ? WHERE book_id = ?";

        // Check if the book exists before updating
        if (!bookExistsById(book.getBookId())) {
            System.out.println("Book does not exist.");
            return false;
        }

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getCategory());
            statement.setInt(5, book.getBookId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a book with validation to check if the book exists
    public static boolean deleteBook(int bookId) {
        String query = "UPDATE Book SET is_archived = ? WHERE book_id = ? AND is_archived = ?";

        // Check if the book exists before deleting
        if (!bookExistsById(bookId)) {
            return false;
        }

        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, true);  // Set is_archived to true
            statement.setInt(2, bookId);
            statement.setBoolean(3, false); // Only update if it's not already archived
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if a book exists by book ID
    public static boolean bookExistsById(int bookId) {
        String query = "SELECT COUNT(*) FROM Book WHERE book_id = ?";
        try (Connection connection = DatabaseSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
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
