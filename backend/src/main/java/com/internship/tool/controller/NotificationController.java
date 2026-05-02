package com.internship.tool.controller;

import com.internship.tool.dto.NotificationResponse;
import com.internship.tool.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for in-app Notifications.
 * Base URL: /api/v1/notifications
 * Day 16 — In-App Notification System
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "In-app KRI breach alert notifications")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * GET /api/v1/notifications
     * Returns all notifications (read + unread) for the authenticated user.
     */
    @GetMapping
    @Operation(summary = "Get all my notifications (read + unread)")
    public ResponseEntity<List<NotificationResponse>> getAll() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    /**
     * GET /api/v1/notifications/unread
     * Returns only unread notifications for the authenticated user.
     */
    @GetMapping("/unread")
    @Operation(summary = "Get my unread notifications")
    public ResponseEntity<List<NotificationResponse>> getUnread() {
        return ResponseEntity.ok(notificationService.getUnread());
    }

    /**
     * GET /api/v1/notifications/unread/count
     * Returns the count of unread notifications.
     */
    @GetMapping("/unread/count")
    @Operation(summary = "Get unread notification count (useful for badge display)")
    public ResponseEntity<Map<String, Long>> countUnread() {
        return ResponseEntity.ok(Map.of("unreadCount", notificationService.countUnread()));
    }

    /**
     * PATCH /api/v1/notifications/{id}/read
     * Mark a single notification as read.
     */
    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a single notification as read")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markRead(id));
    }

    /**
     * PATCH /api/v1/notifications/read-all
     * Mark all notifications as read for the current user.
     */
    @PatchMapping("/read-all")
    @Operation(summary = "Mark all my notifications as read")
    public ResponseEntity<Void> markAllRead() {
        notificationService.markAllRead();
        return ResponseEntity.noContent().build();
    }
}
