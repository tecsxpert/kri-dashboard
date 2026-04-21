-- ============================================================
-- V1__init.sql
-- KRI Dashboard — Initial Schema
-- Author: Varad (Java Developer 2)
-- Day 1 — 14 April 2026
-- ============================================================

-- Core KRI table
CREATE TABLE kri (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255)    NOT NULL,
    description         TEXT,
    category            VARCHAR(100),
    status              VARCHAR(50)     NOT NULL DEFAULT 'ACTIVE',
    score               INTEGER         CHECK (score >= 0 AND score <= 100),
    threshold           INTEGER         CHECK (threshold >= 0 AND threshold <= 100),
    owner               VARCHAR(255),
    department          VARCHAR(255),
    risk_level          VARCHAR(50),
    due_date            DATE,
    ai_description      TEXT,
    ai_recommendation   TEXT,
    is_deleted          BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255)
);

-- Indexes on key lookup fields
CREATE INDEX idx_kri_status      ON kri(status);
CREATE INDEX idx_kri_category    ON kri(category);
CREATE INDEX idx_kri_risk_level  ON kri(risk_level);
CREATE INDEX idx_kri_due_date    ON kri(due_date);
CREATE INDEX idx_kri_created_at  ON kri(created_at);
CREATE INDEX idx_kri_is_deleted  ON kri(is_deleted);
CREATE INDEX idx_kri_owner       ON kri(owner);

-- Composite index for common filtered queries
CREATE INDEX idx_kri_status_deleted ON kri(status, is_deleted);
