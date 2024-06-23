package com.justiceasare.gtplibrary.model;

public class User {
    private int userId;
    private String username;
    private String email;
    private UserType userType;

    public User(int userId, String username, String email, UserType userType) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
