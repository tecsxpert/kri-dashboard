package com.internship.tool.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request DTO for creating or updating a KRI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KriRequest {

    @NotBlank(message = "KRI name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private String description;

    @Pattern(
        regexp = "^(ACTIVE|INACTIVE|BREACH|NEAR_BREACH)$",
        message = "Status must be one of: ACTIVE, INACTIVE, BREACH, NEAR_BREACH"
    )
    private String status;

    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must not exceed 100")
    private Integer score;
}
