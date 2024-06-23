package com.justiceasare.gtplibrary.model;

public class Patron extends User {
    private String patronId;

    public Patron(int userId, String username, String email, UserType userType, String patronId) {
        super(userId, username, email, userType);
        this.patronId = patronId;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }
}
