package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Book;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


public class BookDAOTest {

    private static BookDAO bookDao;
    private static Connection connection;
    private List<Integer> insertedBookIds;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DatabaseSource.getConnection();
        bookDao = new BookDAO(connection);
    }

    @BeforeEach
    void insertTestBooks() throws SQLException {
        insertedBookIds = new ArrayList<>();
        String insertQuery = "INSERT INTO Book (title, author, isbn, category, is_archived) VALUES (?, ?, ?, ?, 0)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            String[][] testBooks = {
                    {"Test Book 1", "Author 1", "1234567890", "Fiction"},
                    {"Test Book 2", "Author 2", "2345678901", "Non-fiction"},
                    {"Test Book 3", "Author 3", "3456789012", "Science"}
            };

            for (String[] book : testBooks) {
                statement.setString(1, book[0]);
                statement.setString(2, book[1]);
                statement.setString(3, book[2]);
                statement.setString(4, book[3]);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    insertedBookIds.add(generatedKeys.getInt(1));
                }
            }
        }
    }

    @AfterEach
    void removeTestBooks() throws SQLException {
        String deleteQuery = "DELETE FROM Book WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            for (int bookId : insertedBookIds) {
                statement.setInt(1, bookId);
                statement.executeUpdate();
            }
        }
        insertedBookIds.clear();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = BookDAO.getAllBooks();
        assertNotNull(books);
    }

    @ParameterizedTest
    @CsvSource ({
                "Test Book 1, 1234567890, true",
                "Test Book 2, 2345678901, true",
                "The Grief Child, 8345678901, false"
        })
    void testBookExists(String title, String isbn, boolean expected) {
        assertEquals(expected, bookDao.bookExists(title, isbn));
    }

    @Test
    void testAddBook() {
        Book newBook = new Book(0, "New Book", "New Author", "3456789012", "Mystery", false);
        int bookId = bookDao.addBook(newBook);
        assertTrue(bookId > 0);
        insertedBookIds.add(bookId); // Add to list for cleanup

        // Try to add the same book again
        int duplicateId =bookDao.addBook(newBook);
        assertEquals(-1, duplicateId);
    }

    @Test
    void testUpdateBook() {
        assumeTrue(!insertedBookIds.isEmpty(), "No books were inserted");
        int bookId = insertedBookIds.get(0);
        Book updatedBook = new Book(bookId, "Updated Book", "Updated Author", "9876543210", "Updated Category", false);
        assertTrue(BookDAO.updateBook(updatedBook));

        // Try to update a non-existent book
        Book nonExistentBook = new Book(-1, "Non-existent", "Author", "1111111111", "Category", false);
        assertFalse(BookDAO.updateBook(nonExistentBook));
    }

    @Test
    void testDeleteBook() {
        assumeTrue(!insertedBookIds.isEmpty(), "No books were inserted");
        int bookId = insertedBookIds.get(0);
        assertTrue(BookDAO.deleteBook(bookId));

        // Try to delete the same book again (it should be marked as archived now)
        assertFalse(BookDAO.deleteBook(bookId));

        // Try to delete a non-existent book
        assertFalse(BookDAO.deleteBook(-1));
    }

    @Test
    void testAddBookWithNullBook() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            BookDAO.addBook(null);
        });
        assertEquals("Book cannot be null", exception.getMessage());
    }

}