-- liquibase formatted sql
-- changeset geobudget:V019
ALTER TABLE categories ADD COLUMN parent_id BIGINT;

CREATE INDEX idx_categories_parent_id ON categories(parent_id);
