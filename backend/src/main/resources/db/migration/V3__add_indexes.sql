-- V3__add_indexes.sql
-- Day 7: Add indexes on kri table for faster filtering and search queries.

CREATE INDEX IF NOT EXISTS idx_kri_status   ON kri (status);
CREATE INDEX IF NOT EXISTS idx_kri_score    ON kri (score);
CREATE INDEX IF NOT EXISTS idx_kri_name     ON kri (name);
