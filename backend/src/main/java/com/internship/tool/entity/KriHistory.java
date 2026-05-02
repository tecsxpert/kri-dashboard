package com.internship.tool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Audit trail entity — records every CREATE, UPDATE, DELETE action on a KRI.
 * Maps to the `kri_history` table (Flyway V5).
 * Day 12 — KRI Audit History
 */
@Entity
@Table(name = "kri_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KriHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the KRI that was changed */
    @Column(name = "kri_id", nullable = false)
    private Long kriId;

    /** Action performed: CREATE | UPDATE | DELETE */
    @Column(nullable = false, length = 20)
    private String action;

    /** Username of the user who made the change */
    @Column(name = "changed_by", length = 50)
    private String changedBy;

    @Column(name = "old_name",   length = 255) private String oldName;
    @Column(name = "new_name",   length = 255) private String newName;
    @Column(name = "old_status", length = 50)  private String oldStatus;
    @Column(name = "new_status", length = 50)  private String newStatus;
    @Column(name = "old_score")                private Integer oldScore;
    @Column(name = "new_score")                private Integer newScore;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @PrePersist
    public void prePersist() {
        if (changedAt == null) changedAt = LocalDateTime.now();
    }
}
