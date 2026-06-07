package com.maildp.repository;

import com.maildp.entity.Device;
import com.maildp.entity.User;
import com.maildp.enums.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByUser(User user);
    List<Device> findByStatus(DeviceStatus status);
    List<Device> findByUserAndStatus(User user, DeviceStatus status);
    boolean existsByMacAddress(String macAddress);
}