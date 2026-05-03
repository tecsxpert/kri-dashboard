-- V6: Add soft-delete support to the kri table
-- Author: Varad (Java Developer 2)
-- Day 14 — Soft Delete / Archiving

ALTER TABLE kri ADD COLUMN IF NOT EXISTS deleted     BOOLEAN   NOT NULL DEFAULT FALSE;
ALTER TABLE kri ADD COLUMN IF NOT EXISTS deleted_at  TIMESTAMP;
ALTER TABLE kri ADD COLUMN IF NOT EXISTS deleted_by  VARCHAR(50);

CREATE INDEX IF NOT EXISTS idx_kri_deleted ON kri (deleted);
