package com.internship.tool.dto;

import lombok.*;

/**
 * Response DTO for the user list returned by Admin APIs.
 * Day 9 — Role-Based Access Control
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
}
