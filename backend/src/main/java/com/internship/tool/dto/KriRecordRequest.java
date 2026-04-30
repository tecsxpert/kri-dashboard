package com.internship.tool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new KRI record")
public class KriRecordRequest {

    @NotBlank(message = "Title must not be blank")
    @Schema(description = "Unique title of the KRI record", example = "Credit Risk Exposure")
    private String title;

    @Schema(description = "Detailed description of the KRI", example = "Measures total credit exposure against approved limits.")
    private String description;

    @NotBlank(message = "Category must not be blank")
    @Schema(description = "Business category of the KRI", example = "Credit")
    private String category;

    @NotBlank(message = "Status must not be blank")
    @Schema(description = "Current status of the KRI", example = "ACTIVE", allowableValues = {"ACTIVE", "COMPLETED", "OVERDUE"})
    private String status;

    @Schema(description = "KRI score (0–100)", example = "85.5")
    private Double score;

    @Schema(description = "Due date in ISO-8601 format", example = "2025-12-31")
    private LocalDate dueDate;
}
