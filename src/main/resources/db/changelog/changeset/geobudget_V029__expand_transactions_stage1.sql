-- liquibase formatted sql
-- changeset geobudget:V029

CREATE INDEX IF NOT EXISTS idx_categories_user_transaction_type ON categories (user_id, transaction_type);
CREATE INDEX IF NOT EXISTS idx_categories_system_transaction_type ON categories (type, transaction_type);

ALTER TABLE transactions ADD COLUMN IF NOT EXISTS latitude NUMERIC(10, 7);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS longitude NUMERIC(10, 7);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS city VARCHAR(120);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS country VARCHAR(120);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS place_id VARCHAR(255);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS location_source VARCHAR(16);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS original_amount NUMERIC(14, 2);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS original_currency VARCHAR(3);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS rate_to_base NUMERIC(14, 6);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS base_amount NUMERIC(14, 2);

UPDATE transactions t
SET category_id = c.id
FROM categories c
WHERE t.category_id IS NULL
  AND t.type = 'income'
  AND c.name = 'Зарплата'
  AND c.transaction_type = 'income';

UPDATE transactions t
SET category_id = c.id
FROM categories c
WHERE t.category_id IS NULL
  AND t.type = 'expense'
  AND c.name = 'Прочие товары и услуги'
  AND c.transaction_type = 'expense';

ALTER TABLE transactions ALTER COLUMN category_id SET NOT NULL;

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_lat_lon_pair CHECK (
        (latitude IS NULL AND longitude IS NULL)
        OR (latitude IS NOT NULL AND longitude IS NOT NULL)
    );

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_lat_range CHECK (latitude IS NULL OR latitude BETWEEN -90 AND 90);

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_lon_range CHECK (longitude IS NULL OR longitude BETWEEN -180 AND 180);

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_location_source CHECK (
        location_source IS NULL OR location_source IN ('gps', 'manual', 'map')
    );

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_original_currency_len CHECK (
        original_currency IS NULL OR char_length(original_currency) = 3
    );

CREATE INDEX IF NOT EXISTS idx_transactions_user_city_date ON transactions (user_id, city, occurred_at DESC);
CREATE INDEX IF NOT EXISTS idx_transactions_user_country_date ON transactions (user_id, country, occurred_at DESC);
