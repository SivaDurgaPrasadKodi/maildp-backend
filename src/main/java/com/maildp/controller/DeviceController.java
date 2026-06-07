package com.maildp.controller;

import com.maildp.dto.request.DeviceRequest;
import com.maildp.dto.response.DeviceResponse;
import com.maildp.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/register")
    public ResponseEntity<DeviceResponse> registerDevice(
            @Valid @RequestBody DeviceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deviceService.registerDevice(request, userDetails.getUsername()));
    }

    @GetMapping("/my-devices")
    public ResponseEntity<List<DeviceResponse>> getMyDevices(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deviceService.getMyDevices(userDetails.getUsername()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DeviceResponse>> getPendingDevices() {
        return ResponseEntity.ok(deviceService.getPendingDevices());
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<DeviceResponse> approveDevice(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deviceService.approveDevice(id, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<DeviceResponse> rejectDevice(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deviceService.rejectDevice(id, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/revoke")
    public ResponseEntity<DeviceResponse> revokeDevice(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deviceService.revokeDevice(id, userDetails.getUsername()));
    }
}