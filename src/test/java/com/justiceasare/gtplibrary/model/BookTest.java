package com.justiceasare.gtplibrary.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book(1, "Effective Java", "Joshua Bloch", "978-0134685991", "Programming", false);
    }

    @AfterEach
    void tearDown() {
        book = null;
    }

    @Test
    void testGetAuthor() {
        assertEquals("Joshua Bloch", book.getAuthor());
    }

    @Test
    void testSetAuthor() {
        book.setAuthor("James Gosling");
        assertEquals("James Gosling", book.getAuthor());
    }

    @Test
    void testGetIsbn() {
        assertEquals("978-0134685991", book.getIsbn());
    }

    @Test
    void testSetIsbn() {
        book.setIsbn("978-0201633610");
        assertEquals("978-0201633610", book.getIsbn());
    }

    @Test
    void testGetBookId() {
        assertEquals(1, book.getBookId());
    }

    @Test
    void testSetBookId() {
        book.setBookId(2);
        assertEquals(2, book.getBookId());
    }

    @Test
    void testGetTitle() {
        assertEquals("Effective Java", book.getTitle());
    }

    @Test
    void testSetTitle() {
        book.setTitle("Java Concurrency in Practice");
        assertEquals("Java Concurrency in Practice", book.getTitle());
    }

    @Test
    void testGetCategory() {
        assertEquals("Programming", book.getCategory());
    }

    @Test
    void testSetCategory() {
        book.setCategory("Software Engineering");
        assertEquals("Software Engineering", book.getCategory());
    }

    @Test
    void testGetIsArchived() {
        assertFalse(book.getIs_archived());
    }

    @Test
    void testSetIsArchived() {
        book.setIs_archived(true);
        assertTrue(book.getIs_archived());
    }
}