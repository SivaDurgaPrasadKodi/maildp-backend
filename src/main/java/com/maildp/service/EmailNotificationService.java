package com.maildp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Async
    public void sendMailNotification(String toEmail,
                                     String senderName,
                                     String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("MailDP: New Mail - " + subject);
            message.setText(
                "Hello,\n\n" +
                "You have received a new internal mail from " +
                senderName + ".\n\n" +
                "Subject: " + subject + "\n\n" +
                "Please login to MailDP to read your mail.\n\n" +
                "MailDP System"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    @Async
    public void sendDeviceStatusNotification(String toEmail,
                                              String deviceName,
                                              String status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("MailDP: Device " + status + " - " + deviceName);
            message.setText(
                "Hello,\n\n" +
                "Your device registration request has been " +
                status.toLowerCase() + ".\n\n" +
                "Device: " + deviceName + "\n" +
                "Status: " + status + "\n\n" +
                "Please login to MailDP for more details.\n\n" +
                "MailDP System"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    @Async
    public void sendSecurityAlertNotification(String toEmail,
                                               String alertTitle,
                                               String severity) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("MailDP: Security Alert - " + alertTitle);
            message.setText(
                "Hello,\n\n" +
                "A new security alert has been triggered.\n\n" +
                "Alert: " + alertTitle + "\n" +
                "Severity: " + severity + "\n\n" +
                "Please login to MailDP to review the alert.\n\n" +
                "MailDP System"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }
}