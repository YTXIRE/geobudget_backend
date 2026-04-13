-- liquibase formatted sql
-- changeset geobudget:V044

ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS recurrence_type VARCHAR(20);
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS day_of_month INTEGER;
