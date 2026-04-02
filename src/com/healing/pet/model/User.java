package com.healing.pet.model;
public class User {
    private String userId;
    private String password;

    public boolean isAdmin() {
        return userId != null && userId.startsWith("G") && userId.length() == 7;
    }

    public boolean isNormalUser() {
        return userId != null && userId.matches("\\d{6}");
    }

    // Getters & Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
