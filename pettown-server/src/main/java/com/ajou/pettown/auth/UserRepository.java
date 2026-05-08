package com.ajou.pettown.auth;

// JPA repository for User entity with login-ID based lookup.
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId); // used for duplicate ID check at registration
    Optional<User> findByUserId(String userId); // used for login and JWT resolution
}
