package com.maildp.repository;

import com.maildp.entity.AuditLog;
import com.maildp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByCreatedAtDesc();
    List<AuditLog> findByUserOrderByCreatedAtDesc(User user);
    List<AuditLog> findByActionOrderByCreatedAtDesc(String action);
    List<AuditLog> findByEntityTypeOrderByCreatedAtDesc(String entityType);
}