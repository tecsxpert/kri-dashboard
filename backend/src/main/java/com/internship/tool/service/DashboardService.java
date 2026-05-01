package com.internship.tool.service;

import com.internship.tool.dto.DashboardSummary;

/**
 * Service interface for Dashboard analytics.
 * Day 10 — Analytics & Reporting
 */
public interface DashboardService {

    /** Build a full summary of KRI stats for the dashboard. */
    DashboardSummary getSummary();
}
