package com.ajou.pettown.auth;

import com.ajou.pettown.auth.dto.LoginRequest;
import com.ajou.pettown.auth.dto.LoginResponse;
import com.ajou.pettown.auth.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ajou.pettown.auth.dto.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 아이디 중복 체크
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkDuplicate(@PathVariable String userId) {
        boolean isDuplicate = authService.checkUserIdDuplicate(userId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    String token = authService.login(request);
    return ResponseEntity.ok(new LoginResponse(token));
    }
}