package com.maildp.service;

import com.maildp.dto.request.DeviceRequest;
import com.maildp.dto.response.DeviceResponse;
import com.maildp.entity.Device;
import com.maildp.entity.User;
import com.maildp.enums.DeviceStatus;
import com.maildp.repository.DeviceRepository;
import com.maildp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public DeviceResponse registerDevice(
            DeviceRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));

        if (request.getMacAddress() != null &&
                deviceRepository.existsByMacAddress(request.getMacAddress())) {
            throw new RuntimeException("Device already registered");
        }

        Device device = Device.builder()
                .user(user)
                .deviceName(request.getDeviceName())
                .deviceType(request.getDeviceType())
                .operatingSystem(request.getOperatingSystem())
                .macAddress(request.getMacAddress())
                .ipAddress(request.getIpAddress())
                .status(DeviceStatus.PENDING)
                .build();

        return toResponse(deviceRepository.save(device));
    }

    public List<DeviceResponse> getMyDevices(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));
        return deviceRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DeviceResponse> getPendingDevices() {
        return deviceRepository.findByStatus(DeviceStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DeviceResponse approveDevice(Long deviceId, String adminEmail) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                    new RuntimeException("Device not found"));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() ->
                    new RuntimeException("Admin not found"));

        device.setStatus(DeviceStatus.APPROVED);
        device.setApprovedBy(admin);
        device.setApprovedAt(LocalDateTime.now());

        return toResponse(deviceRepository.save(device));
    }

    public DeviceResponse rejectDevice(Long deviceId, String adminEmail) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                    new RuntimeException("Device not found"));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() ->
                    new RuntimeException("Admin not found"));

        device.setStatus(DeviceStatus.REJECTED);
        device.setApprovedBy(admin);
        device.setApprovedAt(LocalDateTime.now());

        return toResponse(deviceRepository.save(device));
    }

    public DeviceResponse revokeDevice(Long deviceId, String adminEmail) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                    new RuntimeException("Device not found"));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() ->
                    new RuntimeException("Admin not found"));

        device.setStatus(DeviceStatus.REVOKED);
        device.setApprovedBy(admin);

        return toResponse(deviceRepository.save(device));
    }

    private DeviceResponse toResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceType(device.getDeviceType())
                .operatingSystem(device.getOperatingSystem())
                .macAddress(device.getMacAddress())
                .ipAddress(device.getIpAddress())
                .status(device.getStatus().name())
                .ownerName(device.getUser().getFullName())
                .ownerEmail(device.getUser().getEmail())
                .ownerId(device.getUser().getId())
                .approvedByName(device.getApprovedBy() != null
                    ? device.getApprovedBy().getFullName() : null)
                .registeredAt(device.getRegisteredAt())
                .approvedAt(device.getApprovedAt())
                .build();
    }
}