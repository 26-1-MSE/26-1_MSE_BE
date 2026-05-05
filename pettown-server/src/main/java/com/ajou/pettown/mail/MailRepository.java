package com.ajou.pettown.mail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findByUser_IdOrderByCreatedAtDesc(Long userId);
    boolean existsByUser_IdAndIsRead(Long userId, Boolean isRead);
}
