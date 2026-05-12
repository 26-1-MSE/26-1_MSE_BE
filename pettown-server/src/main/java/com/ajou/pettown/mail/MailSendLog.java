package com.ajou.pettown.mail;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mail_send_logs", indexes = {
        @Index(name = "idx_mail_log_pet_type_date", columnList = "pet_id, trigger_type, sent_date"),
        @Index(name = "idx_mail_log_user_type_date", columnList = "user_id, trigger_type, sent_date")
})
public class MailSendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Long petId; // null for RANDOM trigger (user-level), set for ITEM trigger (pet-level)

    @Column(nullable = false)
    private String triggerType; // "ITEM" or "RANDOM"

    @Column(nullable = false)
    private LocalDate sentDate;
}
