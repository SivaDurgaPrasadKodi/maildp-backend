package com.maildp.controller;

import com.maildp.dto.request.MailRequest;
import com.maildp.dto.response.MailResponse;
import com.maildp.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<MailResponse> sendMail(
            @Valid @RequestBody MailRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            mailService.sendMail(request, userDetails.getUsername()));
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<MailResponse>> getInbox(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            mailService.getInbox(userDetails.getUsername()));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<MailResponse>> getSentMail(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            mailService.getSentMail(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MailResponse> getMailById(@PathVariable Long id) {
        return ResponseEntity.ok(mailService.getMailById(id));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MailResponse> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            mailService.markAsRead(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        mailService.deleteMail(id, userDetails.getUsername());
        return ResponseEntity.ok("Mail deleted successfully");
    }
}