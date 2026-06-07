package com.maildp.entity;

import com.maildp.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "mac_address", unique = true)
    private String macAddress;

    @Column(name = "ip_address")
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status = DeviceStatus.PENDING;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}