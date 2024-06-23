package com.justiceasare.gtplibrary.model;

public class Book {
    private int bookId;
    private String title;
    private String category;

    public Book(int bookId, String title, String category) {
        this.bookId = bookId;
        this.title = title;
        this.category = category;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

