package com.maildp.repository;

import com.maildp.entity.SecurityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityAlertRepository extends JpaRepository<SecurityAlert, Long> {
    List<SecurityAlert> findAllByOrderByCreatedAtDesc();
    List<SecurityAlert> findByStatusOrderByCreatedAtDesc(String status);
    List<SecurityAlert> findBySeverityOrderByCreatedAtDesc(String severity);
}