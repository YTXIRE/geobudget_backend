-- liquibase formatted sql
-- changeset geobudget:V040

ALTER TABLE budgets ADD COLUMN IF NOT EXISTS country VARCHAR(120);
