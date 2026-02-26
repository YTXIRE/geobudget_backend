-- Add icon_id and color_id to categories
ALTER TABLE categories ADD COLUMN icon_id BIGINT REFERENCES icons(id);
ALTER TABLE categories ADD COLUMN color_id BIGINT REFERENCES colors(id);

-- Update existing categories with icon and color ids
UPDATE categories SET icon_id = 1, color_id = 1 WHERE name = 'Продукты';
UPDATE categories SET icon_id = 2, color_id = 9 WHERE name = 'Строительные материалы';
UPDATE categories SET icon_id = 3, color_id = 5 WHERE name = 'Электроника и бытовая техника';
UPDATE categories SET icon_id = 4, color_id = 1 WHERE name = 'Аптеки и медицинские товары';
UPDATE categories SET icon_id = 5, color_id = 2 WHERE name = 'Рестораны и кафе';
UPDATE categories SET icon_id = 6, color_id = 3 WHERE name = 'Финансовые услуги';
UPDATE categories SET icon_id = 7, color_id = 5 WHERE name = 'Одежда и обувь';
UPDATE categories SET icon_id = 8, color_id = 7 WHERE name = 'Спортивные товары';
UPDATE categories SET icon_id = 9, color_id = 2 WHERE name = 'Красота и здоровье';
UPDATE categories SET icon_id = 10, color_id = 11 WHERE name = 'Автомобили и мотоциклы';
UPDATE categories SET icon_id = 11, color_id = 10 WHERE name = 'Прочие товары и услуги';

-- Drop old columns
ALTER TABLE categories DROP COLUMN IF EXISTS icon;
ALTER TABLE categories DROP COLUMN IF EXISTS color;
