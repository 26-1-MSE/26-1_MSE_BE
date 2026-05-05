package com.ajou.pettown.mail;

import com.ajou.pettown.common.dto.ApiResponse;
import com.ajou.pettown.mail.dto.MailDetailResponse;
import com.ajou.pettown.mail.dto.MailListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<MailListResponse>> getMailList(
            @RequestAttribute("userId") String userId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(mailService.getMailList(userId)));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }

    @GetMapping("/{mailId}")
    public ResponseEntity<ApiResponse<MailDetailResponse>> getMailDetail(
            @RequestAttribute("userId") String userId,
            @PathVariable Long mailId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(mailService.getMailDetail(userId, mailId)));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }
}
