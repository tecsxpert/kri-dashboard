package com.internship.tool.dto;

import lombok.*;

import java.util.List;

/**
 * Paginated response wrapper for KRI lists.
 * Day 7 — Pagination & Advanced Filtering
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedKriResponse {

    private List<KriResponse> content;

    /** Current page number (0-based) */
    private int page;

    /** Page size */
    private int size;

    /** Total number of elements */
    private long totalElements;

    /** Total number of pages */
    private int totalPages;

    /** Whether this is the last page */
    private boolean last;
}
