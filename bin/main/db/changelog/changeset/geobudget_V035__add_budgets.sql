-- liquibase formatted sql
-- changeset geobudget:V035

CREATE TABLE IF NOT EXISTS budgets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    period_type VARCHAR(24) NOT NULL,
    amount_limit NUMERIC(14, 2) NOT NULL,
    base_currency VARCHAR(3) NOT NULL,
    scope_type VARCHAR(24) NOT NULL,
    category_id BIGINT,
    region VARCHAR(160),
    city VARCHAR(120),
    starts_at DATE NOT NULL,
    ends_at DATE NOT NULL,
    warning_threshold NUMERIC(5, 2) NOT NULL DEFAULT 0.80,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_budgets_user_active ON budgets(user_id, is_active);
