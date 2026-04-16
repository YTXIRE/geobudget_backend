-- V047: Add location_type and location_value to budgets table
-- Allows multi-scope budgets like "Еда" + "Москва" or "Транспорт" + "Россия"

ALTER TABLE budgets
ADD COLUMN location_type VARCHAR(24),
ADD COLUMN location_value VARCHAR(120);

CREATE INDEX idx_budgets_location ON budgets(location_type, location_value);
