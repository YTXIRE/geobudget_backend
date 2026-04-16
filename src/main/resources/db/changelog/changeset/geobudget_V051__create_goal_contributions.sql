-- Create goal_contributions table
CREATE TABLE goal_contributions (
    id BIGSERIAL PRIMARY KEY,
    goal_id BIGINT NOT NULL REFERENCES savings_goals(id) ON DELETE CASCADE,
    transaction_id BIGINT REFERENCES transactions(id) ON DELETE SET NULL,
    amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'RUB',
    contribution_type VARCHAR(20) NOT NULL DEFAULT 'MANUAL',
    note VARCHAR(255),
    contribution_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_goal_contributions_goal_id ON goal_contributions(goal_id);
CREATE INDEX idx_goal_contributions_transaction_id ON goal_contributions(transaction_id);
CREATE INDEX idx_goal_contributions_date ON goal_contributions(contribution_date);

COMMENT ON TABLE goal_contributions IS 'Contributions made to savings goals';
