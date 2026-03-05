-- liquibase formatted sql
-- changeset geobudget:V023

INSERT INTO groups (name, user_id, is_system)
SELECT 'Спорт', NULL, TRUE
WHERE NOT EXISTS (
    SELECT 1
    FROM groups
    WHERE name = 'Спорт' AND user_id IS NULL AND is_system = TRUE
);

INSERT INTO groups (name, user_id, is_system)
SELECT 'Финансы', NULL, TRUE
WHERE NOT EXISTS (
    SELECT 1
    FROM groups
    WHERE name = 'Финансы' AND user_id IS NULL AND is_system = TRUE
);

INSERT INTO groups (name, user_id, is_system)
SELECT 'Техника', NULL, TRUE
WHERE NOT EXISTS (
    SELECT 1
    FROM groups
    WHERE name = 'Техника' AND user_id IS NULL AND is_system = TRUE
);

UPDATE categories c
SET group_id = g.id
FROM groups g
WHERE c.name = 'Спортивные товары'
  AND g.name = 'Спорт'
  AND g.user_id IS NULL
  AND g.is_system = TRUE;

UPDATE categories c
SET group_id = g.id
FROM groups g
WHERE c.name = 'Финансовые услуги'
  AND g.name = 'Финансы'
  AND g.user_id IS NULL
  AND g.is_system = TRUE;

UPDATE categories c
SET group_id = g.id
FROM groups g
WHERE c.name = 'Электроника и бытовая техника'
  AND g.name = 'Техника'
  AND g.user_id IS NULL
  AND g.is_system = TRUE;
