package com.internship.tool.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO returned to clients for KRI data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KriResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
    private Integer score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
