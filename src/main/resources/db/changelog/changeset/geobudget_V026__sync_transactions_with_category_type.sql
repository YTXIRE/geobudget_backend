-- liquibase formatted sql
-- changeset geobudget:V026

UPDATE transactions t
SET type = c.transaction_type
FROM categories c
WHERE t.category_id = c.id
  AND t.type <> c.transaction_type;
