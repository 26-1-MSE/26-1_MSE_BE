package com.ajou.pettown.auth.dto;

public class RegisterRequest {
    private String userId;
    private String password;
    private String nickname;
    private String shopName;

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getShopName() { return shopName; }
}