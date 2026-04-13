-- V045: Add extensive default categories and groups for expenses and income
-- This migration adds system categories organized by groups

-- Create unique system groups for expenses (use unique names to avoid conflicts)
INSERT INTO groups (name, icon, user_id, is_system) VALUES
('Еда и продукты', 'restaurant', NULL, true),
('Транспорт', 'directions_car', NULL, true),
('Жильё', 'home', NULL, true),
('Покупки', 'shopping_bag', NULL, true),
('Здоровье', 'medical_services', NULL, true),
('Развлечения', 'movie', NULL, true),
('Образование', 'school', NULL, true),
('Путешествия', 'flight', NULL, true),
('Подарки', 'card_giftcard', NULL, true),
('Финансы', 'savings', NULL, true),
('Услуги', 'handyman', NULL, true),
('Бизнес', 'business', NULL, true),
('Животные', 'pets', NULL, true),
('Саморазвитие', 'psychology', NULL, true),
('Прочее расходы', 'more_horiz', NULL, true)
ON CONFLICT DO NOTHING;

-- Create unique system groups for income
INSERT INTO groups (name, icon, user_id, is_system) VALUES
('Трудоустройство', 'work', NULL, true),
('Инвестиции', 'trending_up', NULL, true),
('Имущество', 'real_estate_agent', NULL, true),
('Подарки и выигрыши', 'card_giftcard', NULL, true),
('Кэшбэк и бонусы', 'redeem', NULL, true),
('Возвраты', 'replay', NULL, true),
('Интернет доход', 'language', NULL, true),
('Прочее доходы', 'more_horiz', NULL, true)
ON CONFLICT DO NOTHING;

-- ============================================
-- EXPENSE CATEGORIES
-- ============================================

-- Group: Еда и продукты
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Продукты (супермаркет)', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Продукты (дискаунтер)', 28, 18, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Фермерские продукты', 28, 7, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Рестораны и кафе', 1, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Фастфуд', 2, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Доставка еды', 124, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Доставка продуктов', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Снеки и сладости', 5, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мороженое', 6, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Чай/кофе навынос', 3, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Свежая выпечка', 7, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Алкоголь (домой)', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Алкоголь (бар/ресторан)', 1, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Сигареты и табак', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Вейпы и жидкости', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Энергетики', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Соки и воды', 28, 34, (SELECT id FROM groups WHERE name = 'Еда и продукты' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Транспорт
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Общественный транспорт', 6, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Метро', 11, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Автобус/троллейбус', 6, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электричка', 6, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Такси', 4, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Каршеринг', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аренда авто', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Топливо (АИ-92)', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Топливо (АИ-95)', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Топливо (АИ-98)', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Топливо (дизель)', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроавто (зарядка)', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Автосервис', 19, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Шиномонтаж', 19, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мойка автомобиля', 19, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Парковка', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Платные дороги', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Штрафы ГИБДД', 1, 34, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('ОСАГО', 13, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('КАСКО', 13, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Транспортный налог', 13, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Амортизация авто', 1, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аренда велосипеда', 28, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аренда самоката', 28, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Такси (в путешествии)', 4, 38, (SELECT id FROM groups WHERE name = 'Транспорт' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Жильё
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Аренда квартиры', 5, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аренда комнаты', 5, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ипотека', 5, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Коммунальные услуги', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электричество', 28, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Газ', 28, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Вода', 121, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Отопление', 28, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Вывоз мусора', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Интернет (домашний)', 70, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мобильная связь', 73, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мебель', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Бытовая техника', 64, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ремонт и стройматериалы', 19, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Инструменты', 19, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Уборка (клининг)', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Бытовая химия', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Химчистка', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Прачечная', 4, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Садоводство', 103, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Умный дом', 28, 45, (SELECT id FROM groups WHERE name = 'Жильё' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Покупки
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Одежда (мужская)', 2, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Одежда (женская)', 2, 18, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Одежда (детская)', 2, 19, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Обувь (мужская)', 28, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Обувь (женская)', 28, 18, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Обувь (детская)', 28, 19, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аксессуары (сумки)', 2, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аксессуары (часы)', 67, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аксессуары (ремни/бижутерия)', 28, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроника (телефоны)', 64, 36, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроника (ноутбуки)', 65, 36, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроника (планшеты)', 65, 36, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроника (ТВ)', 66, 36, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроника (аудио)', 66, 36, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Электроника (игровые консоли)', 60, 36, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аптека (лекарства)', 50, 26, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аптека (БАДы)', 51, 26, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аптека (медизделия)', 50, 26, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Косметика (уход)', 46, 18, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Косметика (декоративная)', 46, 18, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Парфюмерия', 78, 18, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ювелирные украшения', 28, 18, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Спорттовары', 59, 50, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Игрушки', 94, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Книги (бумажные)', 52, 52, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Книги (электронные)', 53, 52, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Товары для творчества', 106, 50, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Музыкальные инструменты', 77, 50, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Охота и рыбалка', 28, 50, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Для животных (корма)', 104, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Для животных (зоотовары)', 104, 54, (SELECT id FROM groups WHERE name = 'Покупки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Здоровье
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Терапевт', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Педиатр', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Хирург', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Стоматолог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Офтальмолог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('ЛОР', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Гинеколог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Уролог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Кардиолог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Невролог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Психиатр/психолог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Дерматолог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ортопед', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Маммолог', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Анализы (кровь и т.д.)', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('МРТ/КТ/рентген', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('УЗИ', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Медицинские процедуры', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Массаж', 47, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мануальная терапия', 47, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Иглорефлексотерапия', 47, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('СПА и салоны красоты', 47, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Соляная пещера', 47, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Фитнес/тренажёрный зал', 46, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Бассейн', 59, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Йога/пилатес', 59, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Спортивные секции', 59, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Очки и линзы', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Слуховой аппарат', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Медицинская страховка', 13, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Санаторий', 49, 26, (SELECT id FROM groups WHERE name = 'Здоровье' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Развлечения
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Кино', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Театр', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Концерты', 77, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Цирк', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Музыкальные шоу', 77, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Comedy Club', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Stand-up', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Лекции и meetups', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (Netflix)', 84, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (Spotify)', 76, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (Apple Music)', 76, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (YouTube Premium)', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (VK/Одноклассники)', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (Игровые)', 60, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (Облачные)', 64, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подписки (Новостные)', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Видеоигры', 13, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мобильные игры', 64, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Покупки в играх', 60, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('VR-аттракционы', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Боулинг', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Бильярд', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Настольные игры', 84, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Караоке', 77, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Казино/ставки', 12, 34, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Лотерея', 12, 34, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Парки аттракционов', 100, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Зоопарк', 104, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Музеи', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Выставки', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Фестивали', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ночные клубы', 12, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Бары', 3, 67, (SELECT id FROM groups WHERE name = 'Развлечения' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Образование
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Школа (кружки)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Репетиторство (школьное)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Репетиторство (ЕГЭ/ОГЭ)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (IT)', 54, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (иностранные языки)', 55, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (дизайн)', 106, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (маркетинг)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (финансы)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (кулинария)', 28, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (рукоделие)', 106, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Курсы (авто)', 1, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Онлайн-курсы', 54, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Мастер-классы', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Профпереподготовка', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Высшее образование', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аспирантура', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Книги (учебные)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Канцтовары', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Школьная форма', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Детский сад', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Лагерь (детский)', 52, 59, (SELECT id FROM groups WHERE name = 'Образование' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Путешествия
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Авиабилеты', 6, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ж/Д билеты', 6, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Автобусные билеты', 6, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Паромы', 6, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Отели (бюджет)', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Отели (средний)', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Отели (люкс)', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Хостелы', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Апартаменты (Airbnb)', 5, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Экскурсии', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Гиды', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Входные билеты (достопримечательности)', 56, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Страховка путешествий', 13, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Аренда авто (в путешествии)', 1, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Трансферы', 1, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Сувениры', 2, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Duty Free', 2, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Питание в путешествии', 1, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Развлечения в путешествии', 12, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Виза', 13, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Загранпаспорт', 13, 46, (SELECT id FROM groups WHERE name = 'Путешествия' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Подарки
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Подарки (родным)', 118, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подарки (друзьям)', 118, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подарки (коллегам)', 118, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Подарки (детям)', 118, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Цветы', 103, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Открытки/упаковка', 118, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Благотворительность', 95, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Пожертвования (фонды)', 95, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Церковь/мечеть/синагога', 100, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Милостыня', 95, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Конверты/деньги', 118, 46, (SELECT id FROM groups WHERE name = 'Подарки' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Финансы
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Банковская комиссия', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Перевод (СБП)', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Перевод (банковский)', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Обналичивание', 89, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Инвестиции (покупка акций)', 90, 28, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Инвестиции (покупка облигаций)', 90, 28, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Криптовалюта', 90, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('НДФЛ', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Транспортный налог', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Земельный налог', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Имущественный налог', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Штрафы (не ГИБДД)', 88, 34, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Пошлины', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Оплата пошлин', 88, 54, (SELECT id FROM groups WHERE name = 'Финансы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Услуги
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Почта/доставка', 28, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Нотариус', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Юрист', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Адвокат', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Риелтор', 5, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Оценка имущества', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Грузчики', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Клининг', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Репетитор (музыка)', 77, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Няня', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Сиделка', 19, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Страхование (жизнь)', 13, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Страхование (имущество)', 13, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Страхование (ОСАГО)', 13, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Страхование (КАСКО)', 13, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Страхование (ДМС)', 49, 46, (SELECT id FROM groups WHERE name = 'Услуги' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Бизнес
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Офисные расходы', 112, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Расходные материалы', 112, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Инструменты (проф.)', 19, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Спецодежда', 2, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Корпоративные подарки', 118, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Командировочные', 5, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Конференции', 52, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Бизнес-ланч', 1, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Программное обеспечение', 54, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Хостинг/домены', 70, 45, (SELECT id FROM groups WHERE name = 'Бизнес' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Животные
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Корм для собак', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Корм для кошек', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Корм для птиц', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Корм для рыбок', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Корм для грызунов', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ветеринар (кошки)', 49, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ветеринар (собаки)', 49, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Ветеринар (другие)', 49, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Вакцинация', 49, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Стерилизация', 49, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Зоотовары (игрушки)', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Зоотовары (одежда)', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Зоотовары (переноски)', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Зоотовары (миски/лежанки)', 104, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Груминг (собаки)', 47, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Груминг (кошки)', 47, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Зоогостиница', 4, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Дрессировка', 19, 77, (SELECT id FROM groups WHERE name = 'Животные' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Саморазвитие
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Коучинг', 49, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Тренинги', 52, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Тета-исцеление', 47, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Медитации', 47, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Консультации (психолог)', 49, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Консультации (финансовый советник)', 88, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Консультации (юрист)', 19, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Астрология/нумерология', 49, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Эзотерика', 49, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Антикварные товары', 28, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Коллекционирование', 28, 59, (SELECT id FROM groups WHERE name = 'Саморазвитие' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- Group: Прочее расходы
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Непредвиденные расходы', 118, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Резервный фонд', 88, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Неизвестные расходы', 118, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Наличные (неизвестно куда)', 89, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Долги (возврат)', 88, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Алименты', 88, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL),
('Прочие расходы', 118, 87, (SELECT id FROM groups WHERE name = 'Прочее расходы' AND user_id IS NULL LIMIT 1), 'system', 'expense', NULL)
ON CONFLICT DO NOTHING;

-- ============================================
-- INCOME CATEGORIES
-- ============================================

-- Group: Трудоустройство
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Зарплата (чистыми)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Зарплата (gross)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Аванс', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Зарплата (13-я)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Зарплата (премия)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Зарплата (квартальная)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Зарплата (годовая)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Бонус за производительность', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Overtime', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Комиссионные', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Чаевые', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Фриланс (разовый)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Фриланс (постоянный)', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Подработка', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Работа в выходные', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Удалённая работа', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Вознаграждение за рекомендации', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация за питание', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация за транспорт', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация за связь', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация за жильё', 111, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Путешествия (оплачиваемые)', 5, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Обучение (оплачиваемое)', 52, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Медицинская страховка (от раб.)', 13, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Пенсия (от работодателя)', 88, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Дивиденды (от раб.)', 88, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Выходное пособие', 88, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Отпускные', 88, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Больничный', 49, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Декретные', 49, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Пособие по безработице', 88, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Стипендия', 52, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Гранты', 88, 28, (SELECT id FROM groups WHERE name = 'Трудоустройство' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Инвестиции
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Дивиденды (акции)', 90, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Купоны (облигации)', 90, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Проценты (вклады)', 89, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Проценты (кэшбэк на вклад)', 89, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа акций', 90, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа облигаций', 90, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа ETF', 90, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа криптовалюты', 90, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа недвижимости', 5, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа авто', 1, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа вещей', 2, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход от аренды (квартира)', 5, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход от аренды (комната)', 5, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход от аренды (авто)', 1, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход от аренды (техника)', 64, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Роялти (авторские)', 52, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Роялти (патенты)', 52, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Патентные отчисления', 52, 28, (SELECT id FROM groups WHERE name = 'Инвестиции' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Имущество
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Продажа квартиры', 5, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа дома', 5, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа земли', 100, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа гаража', 5, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа дачи', 5, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа машины', 1, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа мотоцикла', 1, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа велосипеда', 28, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа техники', 64, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа мебели', 4, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа антиквариата', 28, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа коллекций', 28, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Сдача в аренду (квартира)', 5, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Сдача в аренду (комната)', 5, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Сдача в аренду (парковка)', 1, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Сдача в аренду (кладовка)', 4, 28, (SELECT id FROM groups WHERE name = 'Имущество' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Подарки и выигрыши
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Деньги в подарок (НГ)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Деньги в подарок (день рождения)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Деньги в подарок (свадьба)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Деньги в подарок (без события)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Выигрыш (лотерея)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Выигрыш (казино)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Выигрыш (ставки)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Приз (конкурс)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Приз (розыгрыш)', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Наследство', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Завещание', 118, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Страховая выплата (жизнь)', 13, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Страховая выплата (здоровье)', 49, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Страховая выплата (имущество)', 5, 28, (SELECT id FROM groups WHERE name = 'Подарки и выигрыши' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Кэшбэк и бонусы
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Кэшбэк (банковский)', 89, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Кэшбэк (покупки)', 89, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Кэшбэк (сервисы)', 89, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Бонусы (авиамили)', 6, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Бонусы (ж/д)', 6, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Бонусы (отели)', 5, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Бонусы (магазины)', 2, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Мили (авиакомпании)', 6, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Баллы (аптеки)', 50, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Промокоды (возврат)', 89, 28, (SELECT id FROM groups WHERE name = 'Кэшбэк и бонусы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Возвраты
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Возврат товара', 118, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Возврат услуги', 118, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Возврат налога (НДФЛ)', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Возврат налога (имущественный)', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Возврат страховки', 13, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Возврат депозита (аренда)', 5, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Возврат залога', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация (судебная)', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация (от работодателя)', 111, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Компенсация (от государства)', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Алименты (получено)', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Пособие на ребёнка', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Материнский капитал', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Единовременная выплата', 88, 28, (SELECT id FROM groups WHERE name = 'Возвраты' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Интернет доход
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Продажа фото', 108, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа видео', 108, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа музыки', 76, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа текстов', 52, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа ПО', 54, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Продажа игр', 60, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход с блога (реклама)', 70, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход с YouTube', 70, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход с Telegram', 70, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход с TikTok', 70, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход с Instagram', 70, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Доход с маркетплейсов', 2, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Партнёрские программы', 90, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Реферальные бонусы', 118, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Опросы (оплата)', 52, 28, (SELECT id FROM groups WHERE name = 'Интернет доход' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;

-- Group: Прочее доходы
INSERT INTO categories (name, icon_id, color_id, group_id, type, transaction_type, user_id) VALUES
('Неизвестный доход', 118, 28, (SELECT id FROM groups WHERE name = 'Прочее доходы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Найденные деньги', 118, 28, (SELECT id FROM groups WHERE name = 'Прочее доходы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Долг (полученный возврат)', 88, 28, (SELECT id FROM groups WHERE name = 'Прочее доходы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Перевод от другого счёта', 88, 28, (SELECT id FROM groups WHERE name = 'Прочее доходы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL),
('Прочее', 118, 28, (SELECT id FROM groups WHERE name = 'Прочее доходы' AND user_id IS NULL LIMIT 1), 'system', 'income', NULL)
ON CONFLICT DO NOTHING;
