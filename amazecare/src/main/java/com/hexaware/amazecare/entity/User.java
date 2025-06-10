package com.hexaware.amazecare.entity;
import java.time.LocalDateTime;
public class User {
    private int userId;
    private Role role;

    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private LocalDateTime createdAt = LocalDateTime.now();

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
