package com.internship.tool.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Summary DTO for the Dashboard Analytics API.
 * Day 10 — Analytics & Reporting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummary {

    /** Total number of KRIs */
    private long totalKris;

    /** Count per status: ACTIVE, INACTIVE, BREACH, NEAR_BREACH */
    private Map<String, Long> countByStatus;

    /** Average score across all KRIs */
    private double averageScore;

    /** Number of KRIs in BREACH or NEAR_BREACH */
    private long atRiskCount;

    /** Top 5 highest-scoring KRIs (risk leaders) */
    private List<KriResponse> topRiskKris;

    /** Score distribution buckets: 0-25, 26-50, 51-75, 76-100 */
    private Map<String, Long> scoreDistribution;

    /** Percentage of KRIs currently at risk (BREACH + NEAR_BREACH) */
    private double atRiskPercentage;
}
