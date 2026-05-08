package com.ajou.pettown.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminUserDto {
    private Long id;
    private String userId;
    private String nickname;
    private String shopName;
    private int petCount;
    private int itemCount;
    private int mailCount;
    private LocalDateTime lastActiveAt;
}
