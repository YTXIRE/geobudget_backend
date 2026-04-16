-- Create savings_goals table
CREATE TABLE savings_goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    target_amount DECIMAL(15, 2) NOT NULL,
    current_amount DECIMAL(15, 2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'RUB',
    icon VARCHAR(50),
    color VARCHAR(7),
    goal_type VARCHAR(30) NOT NULL DEFAULT 'OTHER',
    priority INT DEFAULT 5 CHECK (priority >= 1 AND priority <= 10),
    target_date DATE,
    deadline_enabled BOOLEAN DEFAULT false,
    monthly_target DECIMAL(15, 2),
    auto_contribution_enabled BOOLEAN DEFAULT false,
    auto_contribution_amount DECIMAL(15, 2),
    auto_contribution_source VARCHAR(20),
    auto_contribution_percent DECIMAL(5, 2),
    contribution_category_id BIGINT REFERENCES categories(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_savings_goals_user_id ON savings_goals(user_id);
CREATE INDEX idx_savings_goals_status ON savings_goals(status);

COMMENT ON TABLE savings_goals IS 'User savings goals and financial targets';
