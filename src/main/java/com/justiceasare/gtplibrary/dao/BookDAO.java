package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Book;
import com.justiceasare.gtplibrary.model.BookCopy;
import com.justiceasare.gtplibrary.model.CopyStatus;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDAO {

    public static List<Book> getAllBooks() {
        String query = "SELECT b.book_id, b.title, b.author, b.isbn, b.category, " +
                "bc.copy_id, bc.status FROM Book b LEFT JOIN BookCopy bc ON b.book_id = bc.book_id";
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
                    book = new Book(bookId, title, author, isbn, category);
                    bookMap.put(bookId, book);
                }

                int copyId = resultSet.getInt("copy_id");
                if (copyId > 0) {
                    String status = resultSet.getString("status");
                    BookCopy copy = new BookCopy(copyId, bookId, CopyStatus.valueOf(status));
                    book.getCopies().add(copy);
                }
            }

            books.addAll(bookMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int addBook(Book book) {
    String insertBookSQL = "INSERT INTO Book (title, author, isbn, category) VALUES (?, ?, ?, ?)";
    String insertCopySQL = "INSERT INTO BookCopy (book_id, status) VALUES (?, ?)";
    int bookId = -1;
    try (Connection connection = DatabaseSource.getConnection();
         PreparedStatement bookStatement = connection.prepareStatement(insertBookSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

        // Insert the book into the Book table
        bookStatement.setString(1, book.getTitle());
        bookStatement.setString(2, book.getAuthor());
        bookStatement.setString(3, book.getIsbn());
        bookStatement.setString(4, book.getCategory());
        int rowsInserted = bookStatement.executeUpdate();

        if (rowsInserted > 0) {
            // Retrieve the generated book ID
            try (ResultSet generatedKeys = bookStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookId = generatedKeys.getInt(1);

                    // Insert the book copy into the BookCopies table
                    try (PreparedStatement copyStatement = connection.prepareStatement(insertCopySQL)) {
                        copyStatement.setInt(1, bookId);
                        copyStatement.setString(2, CopyStatus.AVAILABLE.name());
                        copyStatement.executeUpdate();
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookId;
}

    public static boolean updateBook(Book book) {
        String query = "UPDATE Book SET title = ?, author = ?, isbn = ?, category = ? WHERE book_id = ?";
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
}
