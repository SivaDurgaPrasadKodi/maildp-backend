package com.maildp.repository;

import com.maildp.entity.Mail;
import com.maildp.entity.User;
import com.maildp.enums.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

    List<Mail> findBySenderAndStatusOrderBySentAtDesc(
        User sender, MailStatus status);

    @Query("SELECT m FROM Mail m JOIN m.recipients r " +
           "WHERE r.recipient = :user AND r.isDeleted = false " +
           "ORDER BY m.sentAt DESC")
    List<Mail> findInboxByUser(@Param("user") User user);

    List<Mail> findBySenderOrderBySentAtDesc(User sender);
}