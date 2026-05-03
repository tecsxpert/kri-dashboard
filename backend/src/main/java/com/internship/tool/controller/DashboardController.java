package com.internship.tool.controller;

import com.internship.tool.dto.DashboardSummary;
import com.internship.tool.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Dashboard Analytics.
 * Base URL: /api/v1/dashboard
 * Day 10 — Analytics & Reporting
 *
 * All endpoints require authentication (ADMIN or USER).
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard Analytics", description = "KRI analytics summary and statistics")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/v1/dashboard/summary
     * Returns a full analytics snapshot of all KRIs:
     * - Total count, counts per status
     * - Average score, at-risk count + percentage
     * - Top 5 highest-risk KRIs
     * - Score distribution (0-25, 26-50, 51-75, 76-100)
     */
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get KRI dashboard summary with analytics and statistics")
    public ResponseEntity<DashboardSummary> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }
}
