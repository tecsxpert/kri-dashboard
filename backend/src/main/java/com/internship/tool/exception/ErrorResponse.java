package com.internship.tool.exception;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Standardized error response body.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
