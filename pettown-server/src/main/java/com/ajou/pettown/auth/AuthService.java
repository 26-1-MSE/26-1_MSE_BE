package com.ajou.pettown.auth;

// Service interface defining authentication and registration operations.
import com.ajou.pettown.auth.dto.LoginRequest;
import com.ajou.pettown.auth.dto.LoginResponse;
import com.ajou.pettown.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    boolean checkUserIdDuplicate(String userId);
}
