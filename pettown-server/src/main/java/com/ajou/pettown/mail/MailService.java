package com.ajou.pettown.mail;

// Service interface for mail list retrieval and detail reading.
import com.ajou.pettown.mail.dto.MailDetailResponse;
import com.ajou.pettown.mail.dto.MailListResponse;

public interface MailService {
    MailListResponse getMailList(String userId);
    MailDetailResponse getMailDetail(String userId, Long mailId); // also marks the mail as read
}
