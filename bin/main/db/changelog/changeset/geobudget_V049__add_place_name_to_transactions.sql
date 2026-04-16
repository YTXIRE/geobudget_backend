-- liquibase formatted sql
-- changeset geobudget:V049

ALTER TABLE transactions ADD COLUMN IF NOT EXISTS place_name VARCHAR(500);
