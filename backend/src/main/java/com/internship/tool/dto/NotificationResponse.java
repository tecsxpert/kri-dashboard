package com.internship.tool.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Response DTO for in-app notifications.
 * Day 16 — In-App Notification System
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long          id;
    private Long          kriId;
    private String        kriName;
    private String        message;
    private String        severity;
    private Boolean       isRead;
    private String        recipient;
    private LocalDateTime createdAt;
}
