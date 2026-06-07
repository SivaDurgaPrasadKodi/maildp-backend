package com.maildp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private Long totalUsers;
    private Long activeUsers;
    private Long totalDepartments;
    private Long totalMails;
    private Long totalDevices;
    private Long pendingDevices;
    private Long approvedDevices;
    private Long totalAlerts;
    private Long openAlerts;
    private Long totalIncidents;
    private Long openIncidents;
    private Long totalAuditLogs;
}