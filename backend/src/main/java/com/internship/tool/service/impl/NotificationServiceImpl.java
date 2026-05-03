package com.internship.tool.service.impl;

import com.internship.tool.dto.NotificationResponse;
import com.internship.tool.entity.Kri;
import com.internship.tool.entity.Notification;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.NotificationRepository;
import com.internship.tool.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of NotificationService.
 * Day 16 — In-App Notification System
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void createBreachAlert(Kri kri) {
        String status   = kri.getStatus();
        String severity = "BREACH".equals(status) ? "CRITICAL" : "WARNING";

        String message = "BREACH".equals(status)
                ? String.format("🚨 KRI '%s' has BREACHED its threshold! Score: %d", kri.getName(), kri.getScore())
                : String.format("⚠️ KRI '%s' is NEAR BREACH. Score: %d — immediate review required.", kri.getName(), kri.getScore());

        Notification notification = Notification.builder()
                .kriId(kri.getId())
                .kriName(kri.getName())
                .message(message)
                .severity(severity)
                .isRead(false)
                .recipient(null)   // null = broadcast to all users
                .build();

        notificationRepository.save(notification);
        log.warn("[NOTIFICATION] {} alert created for KRI '{}' (id={})", severity, kri.getName(), kri.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications() {
        String username = currentUsername();
        return notificationRepository.findAllForUser(username)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnread() {
        String username = currentUsername();
        return notificationRepository.findUnreadForUser(username)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread() {
        return notificationRepository.countUnreadForUser(currentUsername());
    }

    @Override
    public void markAllRead() {
        notificationRepository.markAllReadForUser(currentUsername());
        log.info("[NOTIFICATION] Marked all read for user={}", currentUsername());
    }

    @Override
    public NotificationResponse markRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        n.setIsRead(true);
        return toResponse(notificationRepository.save(n));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .kriId(n.getKriId())
                .kriName(n.getKriName())
                .message(n.getMessage())
                .severity(n.getSeverity())
                .isRead(n.getIsRead())
                .recipient(n.getRecipient())
                .createdAt(n.getCreatedAt())
                .build();
    }

    private String currentUsername() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) return auth.getName();
        } catch (Exception ignored) { }
        return "anonymous";
    }
}
