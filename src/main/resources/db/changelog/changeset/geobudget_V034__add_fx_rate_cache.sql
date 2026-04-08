-- liquibase formatted sql
-- changeset geobudget:V034

CREATE TABLE IF NOT EXISTS fx_rate_cache (
    id BIGSERIAL PRIMARY KEY,
    rate_date DATE NOT NULL,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    rate NUMERIC(14, 6) NOT NULL,
    provider VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_fx_rate_cache_pair UNIQUE (rate_date, from_currency, to_currency)
);
