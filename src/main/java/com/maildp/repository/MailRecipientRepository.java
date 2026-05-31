package com.maildp.repository;

import com.maildp.entity.Mail;
import com.maildp.entity.MailRecipient;
import com.maildp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailRecipientRepository extends JpaRepository<MailRecipient, Long> {
    Optional<MailRecipient> findByMailAndRecipient(Mail mail, User recipient);
}