package com.maildp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequest {

    @NotBlank(message = "Device name is required")
    private String deviceName;

    private String deviceType;

    private String operatingSystem;

    private String macAddress;

    private String ipAddress;
}