package com.ajou.pettown.auth;

import com.ajou.pettown.auth.dto.LoginRequest;
import com.ajou.pettown.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    String login(LoginRequest request);
    boolean checkUserIdDuplicate(String userId);
}