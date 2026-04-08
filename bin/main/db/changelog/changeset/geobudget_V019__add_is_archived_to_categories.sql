-- liquibase formatted sql
-- changeset geobudget:V019
ALTER TABLE categories ADD COLUMN is_archived BOOLEAN DEFAULT FALSE;
