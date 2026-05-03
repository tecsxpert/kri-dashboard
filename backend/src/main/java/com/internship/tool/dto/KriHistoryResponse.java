package com.internship.tool.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Response DTO for KRI history/audit trail entries.
 * Day 12 — KRI Audit History
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KriHistoryResponse {
    private Long   id;
    private Long   kriId;
    private String action;
    private String changedBy;
    private String oldName;
    private String newName;
    private String oldStatus;
    private String newStatus;
    private Integer oldScore;
    private Integer newScore;
    private LocalDateTime changedAt;
}
