CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    billing_cycle VARCHAR(20) NOT NULL,
    next_payment_date DATE NOT NULL,
    last_paid_date DATE,
    reminder_days INTEGER NOT NULL DEFAULT 3,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    description VARCHAR(500),
    color VARCHAR(7),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subscriptions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_subscriptions_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_subscriptions_cycle CHECK (billing_cycle IN ('WEEKLY', 'MONTHLY', 'QUARTERLY', 'YEARLY')),
    CONSTRAINT chk_subscriptions_status CHECK (status IN ('ACTIVE', 'PAUSED', 'CANCELLED')),
    CONSTRAINT chk_subscriptions_reminder_days CHECK (reminder_days >= 0 AND reminder_days <= 30)
);

CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_next_payment_date ON subscriptions(next_payment_date);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
