package com.ajou.pettown.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MailListResponse {
    private List<MailItem> mails;

    @Getter
    @AllArgsConstructor
    public static class MailItem {
        private Long mailId;
        private String title;
        private String sender;
        private Boolean isRead;
        private String createdAt;
    }
}
