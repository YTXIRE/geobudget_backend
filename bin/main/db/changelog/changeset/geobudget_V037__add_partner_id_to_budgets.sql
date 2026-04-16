-- liquibase formatted sql
-- changeset geobudget:V037

ALTER TABLE budgets ADD COLUMN IF NOT EXISTS partner_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_budgets_partner_id ON budgets(partner_id);
