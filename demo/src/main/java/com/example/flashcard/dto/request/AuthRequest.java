package com.example.flashcard.dto.request;

public class AuthRequest {
    private String username;
    private String password;
    private String fullName; // Dùng cho lúc đăng ký (Đăng nhập thì bỏ qua)

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}