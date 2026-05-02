package com.internship.tool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * In-app notification entity — auto-created when a KRI status changes to BREACH/NEAR_BREACH.
 * Maps to the `notifications` table (Flyway V7).
 * Day 16 — In-App Notification System
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kri_id", nullable = false)
    private Long kriId;

    @Column(name = "kri_name", nullable = false, length = 255)
    private String kriName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /** Severity: INFO | WARNING | CRITICAL */
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String severity = "INFO";

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    /** Target username — null means broadcast to all */
    @Column(length = 50)
    private String recipient;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (isRead    == null) isRead    = false;
        if (severity  == null) severity  = "INFO";
    }
}
