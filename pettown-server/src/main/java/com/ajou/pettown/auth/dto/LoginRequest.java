package com.ajou.pettown.auth.dto;

public class LoginRequest {
    private String userId;
    private String password;

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
}