-- V4: Seed a default ADMIN user for RBAC testing
-- Author: Varad (Java Developer 2)
-- Day 9 — Role-Based Access Control
-- Password: admin123 (BCrypt hash)

INSERT INTO users (username, password, email, role, created_at, updated_at)
VALUES (
    'admin',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'admin@kri-dashboard.com',
    'ROLE_ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, email, role, created_at, updated_at)
VALUES (
    'viewer',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'viewer@kri-dashboard.com',
    'ROLE_USER',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (username) DO NOTHING;
