package com.ajou.pettown.auth;

// Entity representing a registered user account.
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId; // login ID chosen by the user

    @Column(nullable = false)
    private String password; // BCrypt-encoded password

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String shopName;

    @Column
    private LocalDateTime lastActiveAt; // updated on every successful login

    public void updateLastActiveAt() {
        this.lastActiveAt = LocalDateTime.now();
    }
}
