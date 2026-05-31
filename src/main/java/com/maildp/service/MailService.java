package com.maildp.service;

import com.maildp.dto.request.MailRequest;
import com.maildp.dto.response.MailResponse;
import com.maildp.entity.Mail;
import com.maildp.entity.MailRecipient;
import com.maildp.entity.User;
import com.maildp.enums.MailStatus;
import com.maildp.repository.MailRecipientRepository;
import com.maildp.repository.MailRepository;
import com.maildp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailRepository mailRepository;
    private final MailRecipientRepository mailRecipientRepository;
    private final UserRepository userRepository;

    @Transactional
    public MailResponse sendMail(MailRequest request, String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Mail mail = Mail.builder()
                .subject(request.getSubject())
                .body(request.getBody())
                .sender(sender)
                .status(MailStatus.SENT)
                .isImportant(request.getIsImportant())
                .build();

        Mail savedMail = mailRepository.save(mail);

        List<MailRecipient> recipients = new ArrayList<>();
        for (Long recipientId : request.getRecipientIds()) {
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(() ->
                        new RuntimeException("Recipient not found: " + recipientId));

            MailRecipient mailRecipient = MailRecipient.builder()
                    .mail(savedMail)
                    .recipient(recipient)
                    .isRead(false)
                    .isDeleted(false)
                    .build();
            recipients.add(mailRecipientRepository.save(mailRecipient));
        }

        savedMail.setRecipients(recipients);
        return toResponse(savedMail, false);
    }

    public List<MailResponse> getInbox(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mailRepository.findInboxByUser(user)
                .stream()
                .map(mail -> {
                    MailRecipient mr = mailRecipientRepository
                        .findByMailAndRecipient(mail, user)
                        .orElse(null);
                    boolean isRead = mr != null && mr.getIsRead();
                    return toResponse(mail, isRead);
                })
                .collect(Collectors.toList());
    }

    public List<MailResponse> getSentMail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mailRepository.findBySenderOrderBySentAtDesc(user)
                .stream()
                .map(mail -> toResponse(mail, true))
                .collect(Collectors.toList());
    }

    @Transactional
    public MailResponse markAsRead(Long mailId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new RuntimeException("Mail not found"));

        MailRecipient mr = mailRecipientRepository
                .findByMailAndRecipient(mail, user)
                .orElseThrow(() -> new RuntimeException("Mail not found in inbox"));

        mr.setIsRead(true);
        mr.setReadAt(LocalDateTime.now());
        mailRecipientRepository.save(mr);

        return toResponse(mail, true);
    }

    @Transactional
    public void deleteMail(Long mailId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new RuntimeException("Mail not found"));

        MailRecipient mr = mailRecipientRepository
                .findByMailAndRecipient(mail, user)
                .orElseThrow(() -> new RuntimeException("Mail not found in inbox"));

        mr.setIsDeleted(true);
        mailRecipientRepository.save(mr);
    }

    public MailResponse getMailById(Long mailId) {
        Mail mail = mailRepository.findById(mailId)
                .orElseThrow(() -> new RuntimeException("Mail not found"));
        return toResponse(mail, false);
    }

    private MailResponse toResponse(Mail mail, boolean isRead) {
        List<String> recipientNames = new ArrayList<>();
        List<String> recipientEmails = new ArrayList<>();

        if (mail.getRecipients() != null) {
            for (MailRecipient mr : mail.getRecipients()) {
                if (mr.getRecipient() != null) {
                    recipientNames.add(mr.getRecipient().getFullName());
                    recipientEmails.add(mr.getRecipient().getEmail());
                }
            }
        }

        return MailResponse.builder()
                .id(mail.getId())
                .subject(mail.getSubject())
                .body(mail.getBody())
                .senderName(mail.getSender().getFullName())
                .senderEmail(mail.getSender().getEmail())
                .senderId(mail.getSender().getId())
                .recipientNames(recipientNames)
                .recipientEmails(recipientEmails)
                .isImportant(mail.getIsImportant())
                .isRead(isRead)
                .status(mail.getStatus().name())
                .sentAt(mail.getSentAt())
                .build();
    }
}