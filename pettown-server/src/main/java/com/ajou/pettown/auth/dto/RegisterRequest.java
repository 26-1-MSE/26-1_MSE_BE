package com.ajou.pettown.auth.dto;

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
