CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE okveds (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    category_id INT NOT NULL REFERENCES categories(id)
);

INSERT INTO categories (name, description) VALUES
('Продукты', 'Магазины продуктов питания'),
('Строительные материалы', 'Магазины строительных материалов'),
('Электроника и бытовая техника', 'Магазины электроники и бытовой техники'),
('Аптеки и медицинские товары', 'Аптеки и магазины медицинских товаров'),
('Рестораны и кафе', 'Рестораны, кафе, бары'),
('Финансовые услуги', 'Банки, страховые компании, финансовые услуги'),
('Одежда и обувь', 'Магазины одежды и обуви'),
('Спортивные товары', 'Магазины спортивных товаров'),
('Красота и здоровье', 'Салоны красоты, косметика и парфюмерия'),
('Автомобили и мотоциклы', 'Магазины автомобилей и мотоциклов'),
('Прочие товары и услуги', 'Прочие магазины и услуги');

INSERT INTO okveds (code, name, category_id) VALUES
('47.11', 'Супермаркеты', (SELECT id FROM categories WHERE name='Продукты')),
('47.12', 'Мини-маркеты', (SELECT id FROM categories WHERE name='Продукты')),
('47.19', 'Прочие магазины продуктов питания', (SELECT id FROM categories WHERE name='Продукты')),
('47.21', 'Магазины мяса и мясных продуктов', (SELECT id FROM categories WHERE name='Продукты')),
('47.22', 'Магазины фруктов и овощей', (SELECT id FROM categories WHERE name='Продукты')),
('47.23', 'Магазины напитков', (SELECT id FROM categories WHERE name='Продукты')),
('47.24', 'Магазины хлебобулочных изделий', (SELECT id FROM categories WHERE name='Продукты')),
('47.25', 'Магазины молочных продуктов', (SELECT id FROM categories WHERE name='Продукты')),
('47.26', 'Магазины рыбы и морепродуктов', (SELECT id FROM categories WHERE name='Продукты')),
('47.29', 'Прочие магазины продуктов питания', (SELECT id FROM categories WHERE name='Продукты'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.52', 'Магазины строительных материалов', (SELECT id FROM categories WHERE name='Строительные материалы')),
('47.53', 'Магазины сантехники', (SELECT id FROM categories WHERE name='Строительные материалы')),
('47.59', 'Прочие магазины строительных материалов', (SELECT id FROM categories WHERE name='Строительные материалы'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.42', 'Магазины электротоваров', (SELECT id FROM categories WHERE name='Электроника и бытовая техника')),
('47.43', 'Магазины бытовой техники', (SELECT id FROM categories WHERE name='Электроника и бытовая техника')),
('47.54', 'Магазины компьютерной техники', (SELECT id FROM categories WHERE name='Электроника и бытовая техника'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.73', 'Аптеки', (SELECT id FROM categories WHERE name='Аптеки и медицинские товары')),
('47.74', 'Медицинские магазины', (SELECT id FROM categories WHERE name='Аптеки и медицинские товары'));

INSERT INTO okveds (code, name, category_id) VALUES
('56.10', 'Рестораны и кафе', (SELECT id FROM categories WHERE name='Рестораны и кафе')),
('56.29', 'Прочие заведения питания', (SELECT id FROM categories WHERE name='Рестораны и кафе')),
('56.30', 'Бар/Паб', (SELECT id FROM categories WHERE name='Рестораны и кафе')),
('56.21', 'Кофейни', (SELECT id FROM categories WHERE name='Рестораны и кафе'));

INSERT INTO okveds (code, name, category_id) VALUES
('64.19', 'Банки и финансовые учреждения', (SELECT id FROM categories WHERE name='Финансовые услуги')),
('64.20', 'Страховые компании', (SELECT id FROM categories WHERE name='Финансовые услуги')),
('64.30', 'Брокерские услуги', (SELECT id FROM categories WHERE name='Финансовые услуги')),
('64.99', 'Прочие финансовые услуги', (SELECT id FROM categories WHERE name='Финансовые услуги'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.71', 'Магазины одежды', (SELECT id FROM categories WHERE name='Одежда и обувь')),
('47.72', 'Магазины обуви', (SELECT id FROM categories WHERE name='Одежда и обувь'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.64', 'Спортивные магазины', (SELECT id FROM categories WHERE name='Спортивные товары')),
('47.65', 'Магазины туристического снаряжения', (SELECT id FROM categories WHERE name='Спортивные товары'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.75', 'Косметические магазины', (SELECT id FROM categories WHERE name='Красота и здоровье')),
('47.76', 'Магазины парфюмерии', (SELECT id FROM categories WHERE name='Красота и здоровье')),
('96.02', 'Парикмахерские и салоны красоты', (SELECT id FROM categories WHERE name='Красота и здоровье'));

INSERT INTO okveds (code, name, category_id) VALUES
('45.11', 'Продажа автомобилей', (SELECT id FROM categories WHERE name='Автомобили и мотоциклы')),
('45.19', 'Прочие торговые точки авто', (SELECT id FROM categories WHERE name='Автомобили и мотоциклы')),
('45.20', 'Торговля мотоциклами и мототехникой', (SELECT id FROM categories WHERE name='Автомобили и мотоциклы'));

INSERT INTO okveds (code, name, category_id) VALUES
('47.99', 'Прочие магазины розничной торговли', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.91', 'Торговля сувенирами и подарками', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.92', 'Торговля цветами', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.93', 'Торговля книгами', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.94', 'Торговля музыкальными инструментами', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.95', 'Торговля канцелярией', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.96', 'Торговля бытовой химией', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.97', 'Торговля игрушками', (SELECT id FROM categories WHERE name='Прочие товары и услуги')),
('47.98', 'Прочие специализированные магазины', (SELECT id FROM categories WHERE name='Прочие товары и услуги'));
