-- liquibase formatted sql
-- changeset geobudget:V028

UPDATE categories
SET color_id = (SELECT c.id FROM colors c WHERE c.name = 'Green 600' ORDER BY c.id LIMIT 1)
WHERE name = 'Зарплата'
  AND transaction_type = 'income';

UPDATE categories
SET color_id = (SELECT c.id FROM colors c WHERE c.name = 'Blue 600' ORDER BY c.id LIMIT 1)
WHERE name = 'Инвестиции'
  AND transaction_type = 'income';

UPDATE categories
SET color_id = (SELECT c.id FROM colors c WHERE c.name = 'Orange 600' ORDER BY c.id LIMIT 1)
WHERE name = 'Подарки'
  AND transaction_type = 'income';
