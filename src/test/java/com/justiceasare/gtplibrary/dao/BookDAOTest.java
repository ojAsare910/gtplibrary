package com.justiceasare.gtplibrary.dao;

import com.justiceasare.gtplibrary.model.Book;
import com.justiceasare.gtplibrary.util.DatabaseSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

public class BookDAOTest {

    private static BookDAO bookDao;
    private static Connection connection;
    private List<Integer> insertedBookIds;

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private AutoCloseable mockCloseable;

    void insertTestBooks() throws SQLException {
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

    void removeTestBooks() throws SQLException {
        if (!insertedBookIds.isEmpty()) {
            String deleteQuery = "DELETE FROM Book WHERE book_id IN (" +
                    String.join(",", Collections.nCopies(insertedBookIds.size(), "?")) + ")";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                for (int i = 0; i < insertedBookIds.size(); i++) {
                    statement.setInt(i + 1, insertedBookIds.get(i));
                }
                statement.executeUpdate();
            }
            insertedBookIds.clear();
        }
    }

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DatabaseSource.getConnection();
        bookDao = new BookDAO(connection);
    }

    @BeforeEach
    void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        insertedBookIds = new ArrayList<>();
        insertTestBooks();
        mockCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDownEach() throws Exception {
        removeTestBooks();
        mockCloseable.close();
        reset(mockConnection, mockPreparedStatement, mockResultSet);
    }

    @AfterAll
    static void tearDownAll() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Parameterized Testing
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
    void testAddBook() throws SQLException {
        // Test with real database
        Book newBook = new Book(0, "New Book", "New Author", "3456789012", "Mystery", false);
        int bookId = bookDao.addBook(newBook);
        assertTrue(bookId > 0);
        insertedBookIds.add(bookId); // Add to list for cleanup

        // Try to add the same book again
        int duplicateId = bookDao.addBook(newBook);
        assertEquals(-1, duplicateId);
    }

    @Test
    public void testGetAllBooksReal() throws SQLException {
        List<Book> books = bookDao.getAllBooks();
        assertNotNull(books);
        assertTrue(books.size() >= 3);  // At least our 3 test books should be present
    }

    // Mocking
    @Test
    public void testGetAllBooksMock() throws SQLException {
        // Mock the DatabaseSource.getConnection() method
        try (MockedStatic<DatabaseSource> mockedStatic = mockStatic(DatabaseSource.class)) {
            mockedStatic.when(DatabaseSource::getConnection).thenReturn(mockConnection);

            // Set up the mock behavior
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Set up the mock ResultSet to return true twice and then false
            when(mockResultSet.next()).thenReturn(true, true, false);

            // Set up mock data for two books
            when(mockResultSet.getInt("book_id")).thenReturn(1, 2);
            when(mockResultSet.getString("title")).thenReturn("Mock Book 1", "Mock Book 2");
            when(mockResultSet.getString("author")).thenReturn("Mock Author 1", "Mock Author 2");
            when(mockResultSet.getString("isbn")).thenReturn("1234567890", "0987654321");
            when(mockResultSet.getString("category")).thenReturn("Fiction", "Non-fiction");

            List<Book> mockBooks = BookDAO.getAllBooks();

            assertNotNull(mockBooks);
            assertEquals(2, mockBooks.size(), "Expected 2 books, but got " + mockBooks.size());
            assertEquals("Mock Book 1", mockBooks.get(0).getTitle());
            assertEquals("Mock Book 2", mockBooks.get(1).getTitle());

            verify(mockPreparedStatement, times(1)).executeQuery();
            verify(mockResultSet, times(3)).next();
        }
    }

    // Exception Handling
    @Test
    void testAddBookWithNullBook() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            BookDAO.addBook(null);
        });

        String expectedMessage = "Book cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateBook() {
        assumeTrue(!insertedBookIds.isEmpty(), "No books were inserted");
        int bookId = insertedBookIds.get(0);
        Book updatedBook = new Book(bookId, "Updated Book", "Updated Author", "9876543210", "Updated Category", false);
        assertTrue(bookDao.updateBook(updatedBook));

        // Try to update a non-existent book
        Book nonExistentBook = new Book(-1, "Non-existent", "Author", "1111111111", "Category", false);
        assertFalse(bookDao.updateBook(nonExistentBook));
    }

    @Test
    void testDeleteBook() {
        assumeTrue(!insertedBookIds.isEmpty(), "No books were inserted");
        int bookId = insertedBookIds.get(0);
        assertTrue(bookDao.deleteBook(bookId));

        // Try to delete the same book again (it should be marked as archived now)
        assertFalse(bookDao.deleteBook(bookId));

        // Try to delete a non-existent book
        assertFalse(bookDao.deleteBook(-1));
    }
}