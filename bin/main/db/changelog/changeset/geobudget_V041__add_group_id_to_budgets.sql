-- liquibase formatted sql
-- changeset geobudget:V041

ALTER TABLE budgets ADD COLUMN IF NOT EXISTS group_id BIGINT;
ALTER TABLE budgets ADD CONSTRAINT fk_budgets_group FOREIGN KEY (group_id) REFERENCES categories(id);
