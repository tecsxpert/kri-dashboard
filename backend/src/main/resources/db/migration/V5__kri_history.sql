-- V5: Create kri_history table for audit trail tracking
-- Author: Varad (Java Developer 2)
-- Day 12 — KRI Audit History / Change Tracking

CREATE TABLE kri_history (
    id           SERIAL       PRIMARY KEY,
    kri_id       BIGINT       NOT NULL,
    action       VARCHAR(20)  NOT NULL,          -- CREATE | UPDATE | DELETE
    changed_by   VARCHAR(50),                    -- username of who made the change
    old_name     VARCHAR(255),
    new_name     VARCHAR(255),
    old_status   VARCHAR(50),
    new_status   VARCHAR(50),
    old_score    INTEGER,
    new_score    INTEGER,
    changed_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_kri_history_kri_id    ON kri_history (kri_id);
CREATE INDEX idx_kri_history_changed_at ON kri_history (changed_at DESC);
