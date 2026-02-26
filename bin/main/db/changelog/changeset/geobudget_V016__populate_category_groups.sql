-- Add groups to categories based on category names
UPDATE categories SET group_name = 'Еда' WHERE name = 'Продукты';
UPDATE categories SET group_name = 'Еда' WHERE name = 'Рестораны и кафе';
UPDATE categories SET group_name = 'Транспорт' WHERE name = 'Автомобили и мотоциклы';
UPDATE categories SET group_name = 'Дом' WHERE name = 'Строительные материалы';
UPDATE categories SET group_name = 'Техника' WHERE name = 'Электроника и бытовая техника';
UPDATE categories SET group_name = 'Здоровье' WHERE name = 'Аптеки и медицинские товары';
UPDATE categories SET group_name = 'Здоровье' WHERE name = 'Красота и здоровье';
UPDATE categories SET group_name = 'Спорт' WHERE name = 'Спортивные товары';
UPDATE categories SET group_name = 'Финансы' WHERE name = 'Финансовые услуги';
UPDATE categories SET group_name = 'Покупки' WHERE name = 'Одежда и обувь';
UPDATE categories SET group_name = 'Покупки' WHERE name = 'Прочие товары и услуги';
