-- V7: Create notifications table for in-app KRI breach alerts
-- Author: Varad (Java Developer 2)
-- Day 16 — In-App Notification System

CREATE TABLE notifications (
    id          SERIAL       PRIMARY KEY,
    kri_id      BIGINT       NOT NULL,
    kri_name    VARCHAR(255) NOT NULL,
    message     TEXT         NOT NULL,
    severity    VARCHAR(20)  NOT NULL DEFAULT 'INFO',   -- INFO | WARNING | CRITICAL
    is_read     BOOLEAN      NOT NULL DEFAULT FALSE,
    recipient   VARCHAR(50),                            -- username, NULL = broadcast
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notifications_is_read    ON notifications (is_read);
CREATE INDEX idx_notifications_recipient  ON notifications (recipient);
CREATE INDEX idx_notifications_created_at ON notifications (created_at DESC);
