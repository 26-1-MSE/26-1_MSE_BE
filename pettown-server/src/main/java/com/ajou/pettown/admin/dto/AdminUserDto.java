package com.ajou.pettown.admin.dto;

// DTO representing a user's summary data shown in the admin user list.
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
    private int petCount;    // number of pets owned
    private int itemCount;   // total item quantity across all item types
    private int mailCount;   // total number of mails (read + unread)
    private LocalDateTime lastActiveAt; // timestamp of the user's most recent login
}
