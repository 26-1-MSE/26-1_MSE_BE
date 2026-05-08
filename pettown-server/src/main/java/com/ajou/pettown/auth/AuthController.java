package com.ajou.pettown.auth;

// REST controller handling user registration, login, and ID duplication check.
import com.ajou.pettown.auth.dto.LoginRequest;
import com.ajou.pettown.auth.dto.LoginResponse;
import com.ajou.pettown.auth.dto.RegisterRequest;
import com.ajou.pettown.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Returns true if the given userId is already taken
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkDuplicate(@PathVariable String userId) {
        boolean isDuplicate = authService.checkUserIdDuplicate(userId);
        return ResponseEntity.ok(isDuplicate);
    }

    // Registers a new user; returns fail response on duplicate ID
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok(ApiResponse.ok(null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }

    // Authenticates the user and returns a JWT access token with user info
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
