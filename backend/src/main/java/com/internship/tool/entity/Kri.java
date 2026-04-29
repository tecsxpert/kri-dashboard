package com.internship.tool.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * KRI (Key Risk Indicator) Entity — maps to the `kri` table in PostgreSQL.
 * Inherits created_at and updated_at from AuditableEntity.
 */
@Entity
@Table(name = "kri")
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
}
