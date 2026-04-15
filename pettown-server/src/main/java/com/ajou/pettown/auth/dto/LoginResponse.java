package com.ajou.pettown.auth.dto;

public class LoginResponse {
    private String accessToken;
    private String nickname;
    private String shopName;

    public LoginResponse(String accessToken, String nickname, String shopName) {
        this.accessToken = accessToken;
        this.nickname = nickname;
        this.shopName = shopName;
    }

    public String getAccessToken() { return accessToken; }
    public String getNickname() { return nickname; }
    public String getShopName() { return shopName; }
}