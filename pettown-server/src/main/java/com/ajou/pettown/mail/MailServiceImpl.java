package com.ajou.pettown.mail;

import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.mail.dto.MailDetailResponse;
import com.ajou.pettown.mail.dto.MailListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailServiceImpl implements MailService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailRepository mailRepository;

    @Override
    public MailListResponse getMailList(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        List<MailListResponse.MailItem> mails = mailRepository.findByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(mail -> new MailListResponse.MailItem(
                        mail.getMailId(),
                        mail.getTitle(),
                        mail.getSender(),
                        mail.getIsRead(),
                        mail.getCreatedAt().format(FORMATTER)))
                .collect(Collectors.toList());

        return new MailListResponse(mails);
    }

    @Override
    @Transactional
    public MailDetailResponse getMailDetail(String userId, Long mailId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 우편입니다."));

        if (!mail.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 우편이 아닙니다.");
        }

        mail.markAsRead();

        return new MailDetailResponse(
                mail.getMailId(),
                mail.getTitle(),
                user.getNickname(),
                mail.getSender(),
                mail.getContent(),
                mail.getIsRead(),
                mail.getCreatedAt().format(FORMATTER));
    }
}
