-- liquibase formatted sql
-- changeset geobudget:V043

ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS recurrence_enabled BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS recurrence_days VARCHAR(50);
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS city VARCHAR(100);
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS country VARCHAR(100);
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS region VARCHAR(100);
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION;
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION;
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS place_id VARCHAR(100);
ALTER TABLE transaction_templates ADD COLUMN IF NOT EXISTS location_source VARCHAR(20);
