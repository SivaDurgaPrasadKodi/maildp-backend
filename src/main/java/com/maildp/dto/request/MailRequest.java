package com.maildp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class MailRequest {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    @NotEmpty(message = "At least one recipient is required")
    private List<Long> recipientIds;

    private Boolean isImportant = false;
}