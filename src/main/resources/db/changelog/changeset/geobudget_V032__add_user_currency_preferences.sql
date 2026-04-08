-- liquibase formatted sql
-- changeset geobudget:V032

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS base_currency VARCHAR(3) NOT NULL DEFAULT 'RUB';

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS home_city VARCHAR(120);

UPDATE users
SET home_city = COALESCE(home_city, city)
WHERE home_city IS NULL;
