-- liquibase formatted sql
-- changeset geobudget:V031

ALTER TABLE transactions
    ADD COLUMN IF NOT EXISTS region VARCHAR(160);
