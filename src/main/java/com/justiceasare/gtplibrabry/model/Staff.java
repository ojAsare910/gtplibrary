package com.justiceasare.gtplibrabry.model;

public class Staff extends User {
    private String staffId;

    public Staff(int userId, String username, String email, UserType userType, String staffId) {
        super(userId, username, email, userType);
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                "} " + super.toString();
    }
}

