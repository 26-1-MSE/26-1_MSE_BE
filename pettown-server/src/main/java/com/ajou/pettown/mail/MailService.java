package com.ajou.pettown.mail;

import com.ajou.pettown.mail.dto.MailDetailResponse;
import com.ajou.pettown.mail.dto.MailListResponse;

public interface MailService {
    MailListResponse getMailList(String userId);
    MailDetailResponse getMailDetail(String userId, Long mailId);
}
