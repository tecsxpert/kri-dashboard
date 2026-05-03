package com.internship.tool.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * KRI (Key Risk Indicator) Entity — maps to the `kri` table in PostgreSQL.
 * Inherits created_at and updated_at from AuditableEntity.
 */
@Entity
@Table(name = "kri")
@SQLRestriction("deleted = false")   // Day 14: automatically exclude soft-deleted rows
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kri extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String status;

    private Integer score;

    // Day 14: Soft Delete fields
    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 50)
    private String deletedBy;
}
