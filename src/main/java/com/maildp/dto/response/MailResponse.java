package com.maildp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailResponse {
    private Long id;
    private String subject;
    private String body;
    private String senderName;
    private String senderEmail;
    private Long senderId;
    private List<String> recipientNames;
    private List<String> recipientEmails;
    private Boolean isImportant;
    private Boolean isRead;
    private String status;
    private LocalDateTime sentAt;
}