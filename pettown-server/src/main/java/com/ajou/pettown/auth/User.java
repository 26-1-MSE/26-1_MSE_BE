package com.ajou.pettown.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String shopName;

    public static UserBuilder builder() { return new UserBuilder(); }

    public String getUserId() { return userId; }
    public Long getId() { return id; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getShopName() { return shopName; }

    public static class UserBuilder {
        private String userId;
        private String password;
        private String nickname;
        private String shopName;

        public UserBuilder userId(String userId) { this.userId = userId; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder nickname(String nickname) { this.nickname = nickname; return this; }
        public UserBuilder shopName(String shopName) { this.shopName = shopName; return this; }

        public User build() {
            User user = new User();
            user.userId = this.userId;
            user.password = this.password;
            user.nickname = this.nickname;
            user.shopName = this.shopName;
            return user;
        }
    }
}