package com.ajou.pettown.mail;

// Repository for Mail entity, providing user-scoped queries with read-status filtering and creation-time ordering.
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findByUser_IdOrderByCreatedAtDesc(Long userId);
    boolean existsByUser_IdAndIsRead(Long userId, Boolean isRead);
    void deleteByUser_Id(Long userId);
}
