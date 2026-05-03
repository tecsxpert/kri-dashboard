package com.internship.tool.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO for user login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
