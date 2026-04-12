-- liquibase formatted sql
-- changeset geobudget:V020
CREATE TABLE groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT,
    is_system BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_groups_user_id ON groups(user_id);

-- Insert default system groups based on existing category groups
INSERT INTO groups (name, user_id, is_system) VALUES
('Еда', NULL, TRUE),
('Транспорт', NULL, TRUE),
('Развлечения', NULL, TRUE),
('Покупки', NULL, TRUE),
('Услуги', NULL, TRUE),
('Здоровье', NULL, TRUE),
('Дом', NULL, TRUE),
('Путешествия', NULL, TRUE),
('Образование', NULL, TRUE),
('Другое', NULL, TRUE);
