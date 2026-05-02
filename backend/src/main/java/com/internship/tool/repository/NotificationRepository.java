package com.internship.tool.repository;

import com.internship.tool.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for in-app Notifications.
 * Day 16 — In-App Notification System
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /** All unread notifications for a specific recipient (+ broadcast) */
    @Query("""
            SELECT n FROM Notification n
            WHERE n.isRead = false
              AND (n.recipient = :username OR n.recipient IS NULL)
            ORDER BY n.createdAt DESC
            """)
    List<Notification> findUnreadForUser(@Param("username") String username);

    /** All notifications for a user (read + unread), newest first */
    @Query("""
            SELECT n FROM Notification n
            WHERE (n.recipient = :username OR n.recipient IS NULL)
            ORDER BY n.createdAt DESC
            """)
    List<Notification> findAllForUser(@Param("username") String username);

    /** Count unread notifications for a user */
    @Query("""
            SELECT COUNT(n) FROM Notification n
            WHERE n.isRead = false
              AND (n.recipient = :username OR n.recipient IS NULL)
            """)
    long countUnreadForUser(@Param("username") String username);

    /** Mark all of a user's notifications as read */
    @Modifying
    @Query("""
            UPDATE Notification n SET n.isRead = true
            WHERE n.isRead = false
              AND (n.recipient = :username OR n.recipient IS NULL)
            """)
    void markAllReadForUser(@Param("username") String username);
}
