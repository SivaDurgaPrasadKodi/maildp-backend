package com.maildp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private Long id;
    private String deviceName;
    private String deviceType;
    private String operatingSystem;
    private String macAddress;
    private String ipAddress;
    private String status;
    private String ownerName;
    private String ownerEmail;
    private Long ownerId;
    private String approvedByName;
    private LocalDateTime registeredAt;
    private LocalDateTime approvedAt;
}