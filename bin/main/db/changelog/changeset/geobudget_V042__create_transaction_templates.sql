-- liquibase formatted sql
-- changeset geobudget:V042

CREATE TABLE IF NOT EXISTS transaction_templates (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(16) NOT NULL,
    category_id BIGINT,
    amount NUMERIC(14, 2),
    currency VARCHAR(3),
    description VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_templates_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_templates_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT chk_templates_type CHECK (type IN ('income', 'expense'))
);

CREATE INDEX idx_templates_user_id ON transaction_templates (user_id);
CREATE INDEX idx_templates_user_active ON transaction_templates (user_id, is_active);
