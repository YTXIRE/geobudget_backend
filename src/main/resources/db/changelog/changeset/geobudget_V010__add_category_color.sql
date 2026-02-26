-- add color to categories
ALTER TABLE categories ADD COLUMN color BIGINT;

-- add colors to categories (ARGB format)
UPDATE categories SET color = 4294946320 WHERE name = 'Продукты';
UPDATE categories SET color = 4294946800 WHERE name = 'Строительные материалы';
UPDATE categories SET color = 4294964014 WHERE name = 'Электроника и бытовая техника';
UPDATE categories SET color = 4294198070 WHERE name = 'Аптеки и медицинские товары';
UPDATE categories SET color = 4293467223 WHERE name = 'Рестораны и кафе';
UPDATE categories SET color = 4288421424 WHERE name = 'Финансовые услуги';
UPDATE categories SET color = 4294926828 WHERE name = 'Одежда и обувь';
UPDATE categories SET color = 4288781642 WHERE name = 'Спортивные товары';
UPDATE categories SET color = 4294928257 WHERE name = 'Красота и здоровье';
UPDATE categories SET color = 4283916907 WHERE name = 'Автомобили и мотоциклы';
UPDATE categories SET color = 4287893992 WHERE name = 'Прочие товары и услуги';
