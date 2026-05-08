package com.ajou.pettown.mail.dto;

// Response containing the full content of a single mail, returned when a user opens it.
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailDetailResponse {
    private Long mailId;
    private String title;
    private String nickname;  // recipient's nickname
    private String sender;
    private String content;
    private Boolean isRead;
    private String createdAt;
}
