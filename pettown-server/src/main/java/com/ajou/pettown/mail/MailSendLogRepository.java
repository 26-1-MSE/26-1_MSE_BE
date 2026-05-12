package com.ajou.pettown.mail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MailSendLogRepository extends JpaRepository<MailSendLog, Long> {

    boolean existsByPetIdAndTriggerTypeAndSentDate(Long petId, String triggerType, LocalDate sentDate);

    boolean existsByUserIdAndTriggerTypeAndSentDate(Long userId, String triggerType, LocalDate sentDate);
}
