package com.ajou.pettown.auth.dto;

// Request body for the registration endpoint.
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String userId;
    private String password;
    private String nickname;
    private String shopName;
}
