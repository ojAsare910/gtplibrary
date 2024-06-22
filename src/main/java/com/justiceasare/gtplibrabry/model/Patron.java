package com.justiceasare.gtplibrabry.model;

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

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                "} " + super.toString();
    }
}
