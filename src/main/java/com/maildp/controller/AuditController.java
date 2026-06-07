package com.maildp.controller;

import com.maildp.dto.response.AuditLogResponse;
import com.maildp.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(auditService.getLogsByUser(userId));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByAction(
            @PathVariable String action) {
        return ResponseEntity.ok(auditService.getLogsByAction(action));
    }
}