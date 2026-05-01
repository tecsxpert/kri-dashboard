package com.internship.tool.dto;

import lombok.*;

/**
 * Response DTO returned after successful authentication.
 * Contains the JWT token and user metadata.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String username;
    private String role;
    private long expiresIn;
}
