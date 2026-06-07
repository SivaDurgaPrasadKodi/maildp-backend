package com.maildp.service;

import com.maildp.dto.request.SecurityAlertRequest;
import com.maildp.dto.response.IncidentResponse;
import com.maildp.dto.response.SecurityAlertResponse;
import com.maildp.entity.Incident;
import com.maildp.entity.SecurityAlert;
import com.maildp.entity.User;
import com.maildp.repository.IncidentRepository;
import com.maildp.repository.SecurityAlertRepository;
import com.maildp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityAlertService {

    private final SecurityAlertRepository alertRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public SecurityAlertResponse createAlert(
            SecurityAlertRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SecurityAlert alert = SecurityAlert.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .severity(request.getSeverity())
                .status("OPEN")
                .triggeredBy(user)
                .build();

        return toAlertResponse(alertRepository.save(alert));
    }

    public List<SecurityAlertResponse> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toAlertResponse)
                .collect(Collectors.toList());
    }

    public List<SecurityAlertResponse> getAlertsByStatus(String status) {
        return alertRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(this::toAlertResponse)
                .collect(Collectors.toList());
    }

    public SecurityAlertResponse resolveAlert(Long alertId) {
        SecurityAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setStatus("RESOLVED");
        alert.setResolvedAt(LocalDateTime.now());
        return toAlertResponse(alertRepository.save(alert));
    }

    public SecurityAlertResponse acknowledgeAlert(Long alertId) {
        SecurityAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setStatus("ACKNOWLEDGED");
        return toAlertResponse(alertRepository.save(alert));
    }

    public IncidentResponse createIncidentFromAlert(
            Long alertId, String userEmail) {

        SecurityAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Incident incident = Incident.builder()
                .title("Incident: " + alert.getTitle())
                .description(alert.getDescription())
                .severity(alert.getSeverity())
                .status("OPEN")
                .reportedBy(user)
                .alert(alert)
                .build();

        return toIncidentResponse(incidentRepository.save(incident));
    }

    public List<IncidentResponse> getAllIncidents() {
        return incidentRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toIncidentResponse)
                .collect(Collectors.toList());
    }

    public IncidentResponse updateIncidentStatus(Long incidentId, String status) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(status);
        if (status.equals("RESOLVED") || status.equals("CLOSED")) {
            incident.setResolvedAt(LocalDateTime.now());
        }
        return toIncidentResponse(incidentRepository.save(incident));
    }

    private SecurityAlertResponse toAlertResponse(SecurityAlert alert) {
        return SecurityAlertResponse.builder()
                .id(alert.getId())
                .title(alert.getTitle())
                .description(alert.getDescription())
                .severity(alert.getSeverity())
                .status(alert.getStatus())
                .triggeredByName(alert.getTriggeredBy() != null
                    ? alert.getTriggeredBy().getFullName() : null)
                .triggeredByEmail(alert.getTriggeredBy() != null
                    ? alert.getTriggeredBy().getEmail() : null)
                .createdAt(alert.getCreatedAt())
                .resolvedAt(alert.getResolvedAt())
                .build();
    }

    private IncidentResponse toIncidentResponse(Incident incident) {
        return IncidentResponse.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .severity(incident.getSeverity())
                .status(incident.getStatus())
                .reportedByName(incident.getReportedBy() != null
                    ? incident.getReportedBy().getFullName() : null)
                .assignedToName(incident.getAssignedTo() != null
                    ? incident.getAssignedTo().getFullName() : null)
                .alertId(incident.getAlert() != null
                    ? incident.getAlert().getId() : null)
                .createdAt(incident.getCreatedAt())
                .updatedAt(incident.getUpdatedAt())
                .resolvedAt(incident.getResolvedAt())
                .build();
    }
}