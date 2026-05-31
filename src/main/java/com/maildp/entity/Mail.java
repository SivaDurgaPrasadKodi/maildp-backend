package com.maildp.entity;

import com.maildp.enums.MailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body", columnDefinition = "LONGTEXT")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MailStatus status = MailStatus.SENT;

    @Column(name = "is_important")
    private Boolean isImportant = false;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL)
    private List<MailRecipient> recipients;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        sentAt = LocalDateTime.now();
    }
}