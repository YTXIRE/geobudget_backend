-- liquibase formatted sql
-- changeset geobudget:V027

UPDATE categories c
SET group_id = (
    SELECT g.id
    FROM groups g
    WHERE g.name = 'Финансы'
      AND g.user_id IS NULL
      AND g.is_system = TRUE
    ORDER BY g.id
    LIMIT 1
)
WHERE c.name IN ('Зарплата', 'Подарки', 'Инвестиции')
  AND c.transaction_type = 'income';

UPDATE categories
SET icon_id = (SELECT i.id FROM icons i WHERE i.name = 'payments' ORDER BY i.id LIMIT 1)
WHERE name = 'Зарплата'
  AND transaction_type = 'income';

UPDATE categories
SET icon_id = (SELECT i.id FROM icons i WHERE i.name = 'savings' ORDER BY i.id LIMIT 1)
WHERE name = 'Инвестиции'
  AND transaction_type = 'income';

UPDATE categories
SET icon_id = (SELECT i.id FROM icons i WHERE i.name = 'attach_money' ORDER BY i.id LIMIT 1)
WHERE name = 'Подарки'
  AND transaction_type = 'income';
