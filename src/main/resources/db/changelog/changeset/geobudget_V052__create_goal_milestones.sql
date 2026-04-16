-- Create goal_milestones table
CREATE TABLE goal_milestones (
    id BIGSERIAL PRIMARY KEY,
    goal_id BIGINT NOT NULL REFERENCES savings_goals(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    target_amount DECIMAL(15, 2) NOT NULL,
    target_date DATE,
    is_completed BOOLEAN DEFAULT false,
    completed_at TIMESTAMP,
    order_index INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_goal_milestones_goal_id ON goal_milestones(goal_id);

COMMENT ON TABLE goal_milestones IS 'Milestones within savings goals';
