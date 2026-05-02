package com.internship.tool.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for bulk updating KRI statuses.
 * Day 18 — Bulk Operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkStatusUpdateRequest {

    @NotEmpty(message = "List of KRI IDs cannot be empty")
    private List<Long> ids;

    @NotNull(message = "New status cannot be null")
    private String newStatus;
}
