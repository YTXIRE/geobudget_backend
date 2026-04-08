-- liquibase formatted sql
-- changeset geobudget:V024

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(16) NOT NULL,
    amount NUMERIC(14, 2) NOT NULL,
    category_id BIGINT,
    description VARCHAR(255),
    occurred_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_transactions_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transactions_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT chk_transactions_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_transactions_type CHECK (type IN ('income', 'expense')),
    CONSTRAINT chk_expense_category_required CHECK (type = 'income' OR category_id IS NOT NULL)
);

CREATE INDEX idx_transactions_user_date ON transactions (user_id, occurred_at DESC);
CREATE INDEX idx_transactions_user_type_date ON transactions (user_id, type, occurred_at DESC);
CREATE INDEX idx_transactions_user_category_date ON transactions (user_id, category_id, occurred_at DESC);
