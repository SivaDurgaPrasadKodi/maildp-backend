package com.maildp.service;

import com.maildp.dto.response.AuditLogResponse;
import com.maildp.dto.response.DashboardStatsResponse;
import com.maildp.enums.DeviceStatus;
import com.maildp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final MailRepository mailRepository;
    private final DeviceRepository deviceRepository;
    private final SecurityAlertRepository alertRepository;
    private final IncidentRepository incidentRepository;
    private final AuditLogRepository auditLogRepository;

    public DashboardStatsResponse getStats() {

        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findAll()
                .stream()
                .filter(u -> u.getIsActive() != null && u.getIsActive())
                .count();
        long totalDepartments = departmentRepository.count();
        long totalMails = mailRepository.count();
        long totalDevices = deviceRepository.count();
        long pendingDevices = deviceRepository
                .findByStatus(DeviceStatus.PENDING).size();
        long approvedDevices = deviceRepository
                .findByStatus(DeviceStatus.APPROVED).size();
        long totalAlerts = alertRepository.count();
        long openAlerts = alertRepository
                .findByStatusOrderByCreatedAtDesc("OPEN").size();
        long totalIncidents = incidentRepository.count();
        long openIncidents = incidentRepository
                .findByStatusOrderByCreatedAtDesc("OPEN").size();
        long totalAuditLogs = auditLogRepository.count();

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .totalDepartments(totalDepartments)
                .totalMails(totalMails)
                .totalDevices(totalDevices)
                .pendingDevices(pendingDevices)
                .approvedDevices(approvedDevices)
                .totalAlerts(totalAlerts)
                .openAlerts(openAlerts)
                .totalIncidents(totalIncidents)
                .openIncidents(openIncidents)
                .totalAuditLogs(totalAuditLogs)
                .build();
    }

    public List<AuditLogResponse> getRecentActivity() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(10)
                .map(log -> AuditLogResponse.builder()
                        .id(log.getId())
                        .userName(log.getUser() != null
                            ? log.getUser().getFullName() : "System")
                        .userEmail(log.getUser() != null
                            ? log.getUser().getEmail() : "system")
                        .action(log.getAction())
                        .description(log.getDescription())
                        .createdAt(log.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}