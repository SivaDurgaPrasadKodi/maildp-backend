package com.maildp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAlertResponse {
    private Long id;
    private String title;
    private String description;
    private String severity;
    private String status;
    private String triggeredByName;
    private String triggeredByEmail;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}