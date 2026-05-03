package com.internship.tool.dto;

import lombok.*;

/**
 * Filter + pagination request DTO for KRI search.
 * Day 7 — Pagination & Advanced Filtering
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KriFilterRequest {

    /** Partial name search (case-insensitive) */
    private String name;

    /** Filter by exact status */
    private String status;

    /** Minimum score (inclusive) */
    private Integer minScore;

    /** Maximum score (inclusive) */
    private Integer maxScore;

    /** Page number (0-based), default 0 */
    @Builder.Default
    private int page = 0;

    /** Page size, default 10 */
    @Builder.Default
    private int size = 10;

    /** Sort field: id | name | score | status, default id */
    @Builder.Default
    private String sortBy = "id";

    /** Sort direction: asc | desc, default asc */
    @Builder.Default
    private String sortDir = "asc";
}
