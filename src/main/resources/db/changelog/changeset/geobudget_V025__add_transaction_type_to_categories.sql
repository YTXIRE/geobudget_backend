-- liquibase formatted sql
-- changeset geobudget:V025

ALTER TABLE categories ADD COLUMN transaction_type VARCHAR(16);

UPDATE categories
SET transaction_type = 'expense'
WHERE transaction_type IS NULL;

INSERT INTO categories (name, description, type, user_id, transaction_type)
SELECT 'Зарплата', 'Основной доход', 'system', NULL, 'income'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Зарплата');

INSERT INTO categories (name, description, type, user_id, transaction_type)
SELECT 'Подарки', 'Подарки и бонусы', 'system', NULL, 'income'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Подарки');

INSERT INTO categories (name, description, type, user_id, transaction_type)
SELECT 'Инвестиции', 'Доход от инвестиций', 'system', NULL, 'income'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Инвестиции');

ALTER TABLE categories
    ALTER COLUMN transaction_type SET NOT NULL;

ALTER TABLE categories
    ADD CONSTRAINT chk_categories_transaction_type CHECK (transaction_type IN ('income', 'expense'));

CREATE INDEX idx_categories_user_transaction_type ON categories (user_id, transaction_type);
CREATE INDEX idx_categories_transaction_type ON categories (transaction_type);
