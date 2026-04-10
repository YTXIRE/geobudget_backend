-- Add icons to system groups (Flutter Material Icons code points)
UPDATE groups SET icon = '0xe56c' WHERE name = 'Еда' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe531' WHERE name = 'Транспорт' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe40f' WHERE name = 'Развлечения' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xf1cc' WHERE name = 'Покупки' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe059' WHERE name = 'Услуги' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xf179' WHERE name = 'Здоровье' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe318' WHERE name = 'Дом' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe539' WHERE name = 'Путешествия' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe80c' WHERE name = 'Образование' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xea27' WHERE name = 'Спорт' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xf1b6' WHERE name = 'Финансы' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xf30f' WHERE name = 'Техника' AND is_system = TRUE AND user_id IS NULL;
UPDATE groups SET icon = '0xe5d3' WHERE name = 'Другое' AND is_system = TRUE AND user_id IS NULL;
