package com.justiceasare.gtplibrary.model;

public class BookCopy {
    private int copyId;
    private int bookId;
    private CopyStatus status;

    public BookCopy(int copyId, int bookId, CopyStatus status) {
        this.copyId = copyId;
        this.bookId = bookId;
        this.status = status;
    }

    // Getters and Setters
    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }
}

