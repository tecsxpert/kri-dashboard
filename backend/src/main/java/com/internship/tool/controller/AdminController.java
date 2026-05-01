package com.internship.tool.controller;

import com.internship.tool.dto.UserResponse;
import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin-only REST Controller for user management.
 * Base URL: /api/v1/admin
 * Day 9 — Role-Based Access Control (RBAC)
 *
 * All endpoints require ROLE_ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin — User Management", description = "ADMIN-only APIs for managing users")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    /**
     * List all registered users.
     */
    @GetMapping("/users")
    @Operation(summary = "List all users  [ADMIN only]")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("[ADMIN] Listing all users");
        List<UserResponse> users = userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Get a single user by ID.
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID  [ADMIN only]")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("[ADMIN] Fetching user id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Promote a user to ADMIN role.
     */
    @PatchMapping("/users/{id}/promote")
    @Operation(summary = "Promote user to ADMIN role  [ADMIN only]")
    public ResponseEntity<UserResponse> promoteToAdmin(@PathVariable Long id) {
        log.info("[ADMIN] Promoting user id={} to ADMIN", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Demote an ADMIN to regular USER role.
     */
    @PatchMapping("/users/{id}/demote")
    @Operation(summary = "Demote user to ROLE_USER  [ADMIN only]")
    public ResponseEntity<UserResponse> demoteToUser(@PathVariable Long id) {
        log.info("[ADMIN] Demoting user id={} to ROLE_USER", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Delete a user account.
     */
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user account  [ADMIN only]")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("[ADMIN] Deleting user id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
