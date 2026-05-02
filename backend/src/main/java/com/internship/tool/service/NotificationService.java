package com.internship.tool.service;

import com.internship.tool.dto.NotificationResponse;
import com.internship.tool.entity.Kri;

import java.util.List;

/**
 * Service interface for in-app notifications.
 * Day 16 — In-App Notification System
 */
public interface NotificationService {

    /** Auto-create a notification when a KRI status becomes BREACH or NEAR_BREACH */
    void createBreachAlert(Kri kri);

    /** Get all notifications for the current user (read + unread) */
    List<NotificationResponse> getMyNotifications();

    /** Get only unread notifications for the current user */
    List<NotificationResponse> getUnread();

    /** Count unread notifications for the current user */
    long countUnread();

    /** Mark all of the current user's notifications as read */
    void markAllRead();

    /** Mark a single notification as read */
    NotificationResponse markRead(Long id);
}
