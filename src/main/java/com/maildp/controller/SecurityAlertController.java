package com.maildp.controller;

import com.maildp.dto.request.SecurityAlertRequest;
import com.maildp.dto.response.IncidentResponse;
import com.maildp.dto.response.SecurityAlertResponse;
import com.maildp.service.SecurityAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityAlertController {

    private final SecurityAlertService securityAlertService;

    @PostMapping("/alerts")
    public ResponseEntity<SecurityAlertResponse> createAlert(
            @Valid @RequestBody SecurityAlertRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            securityAlertService.createAlert(
                request, userDetails.getUsername()));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<SecurityAlertResponse>> getAllAlerts() {
        return ResponseEntity.ok(securityAlertService.getAllAlerts());
    }

    @GetMapping("/alerts/status/{status}")
    public ResponseEntity<List<SecurityAlertResponse>> getAlertsByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
            securityAlertService.getAlertsByStatus(status));
    }

    @PatchMapping("/alerts/{id}/acknowledge")
    public ResponseEntity<SecurityAlertResponse> acknowledgeAlert(
            @PathVariable Long id) {
        return ResponseEntity.ok(securityAlertService.acknowledgeAlert(id));
    }

    @PatchMapping("/alerts/{id}/resolve")
    public ResponseEntity<SecurityAlertResponse> resolveAlert(
            @PathVariable Long id) {
        return ResponseEntity.ok(securityAlertService.resolveAlert(id));
    }

    @PostMapping("/alerts/{id}/incident")
    public ResponseEntity<IncidentResponse> createIncident(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            securityAlertService.createIncidentFromAlert(
                id, userDetails.getUsername()));
    }

    @GetMapping("/incidents")
    public ResponseEntity<List<IncidentResponse>> getAllIncidents() {
        return ResponseEntity.ok(securityAlertService.getAllIncidents());
    }

    @PatchMapping("/incidents/{id}/status")
    public ResponseEntity<IncidentResponse> updateIncidentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(
            securityAlertService.updateIncidentStatus(id, status));
    }
}