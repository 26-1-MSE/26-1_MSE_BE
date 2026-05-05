package com.ajou.pettown.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailDetailResponse {
    private Long mailId;
    private String title;
    private String nickname;
    private String sender;
    private String content;
    private Boolean isRead;
    private String createdAt;
}
