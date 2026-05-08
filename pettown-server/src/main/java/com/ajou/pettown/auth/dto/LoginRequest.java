package com.ajou.pettown.auth.dto;

// Request body for the login endpoint containing user credentials.
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String userId;
    private String password;
}
