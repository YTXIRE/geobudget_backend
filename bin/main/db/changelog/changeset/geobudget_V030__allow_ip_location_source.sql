-- liquibase formatted sql
-- changeset geobudget:V030

ALTER TABLE transactions DROP CONSTRAINT IF EXISTS chk_transactions_location_source;

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_location_source CHECK (
        location_source IS NULL OR location_source IN ('gps', 'manual', 'map', 'ip')
    );
