package com.maildp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SecurityAlertRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String severity = "MEDIUM";
}