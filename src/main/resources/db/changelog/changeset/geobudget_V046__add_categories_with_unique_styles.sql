-- V046: Update categories with unique icons and colors
-- Updates existing categories from V045 with new icon/color combinations

-- ============================================
-- EXPENSE CATEGORIES
-- ============================================

-- Еда и продукты
UPDATE categories SET icon_id = 21, color_id = 49 WHERE name = 'Продукты (супермаркет)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 27, color_id = 65 WHERE name = 'Продукты (дискаунтер)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 23, color_id = 52 WHERE name = 'Фермерские продукты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 26, color_id = 1 WHERE name = 'Рестораны и кафе' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 27, color_id = 68 WHERE name = 'Фастфуд' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 28, color_id = 5 WHERE name = 'Доставка еды' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 51 WHERE name = 'Доставка продуктов' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 31, color_id = 9 WHERE name = 'Снеки и сладости' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 30, color_id = 11 WHERE name = 'Мороженое' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 29, color_id = 75 WHERE name = 'Чай/кофе навынос' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 32, color_id = 69 WHERE name = 'Свежая выпечка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 33, color_id = 2 WHERE name = 'Алкоголь (домой)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 17 WHERE name = 'Алкоголь (бар/ресторан)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 119, color_id = 34 WHERE name = 'Сигареты и табак' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 116, color_id = 89 WHERE name = 'Энергетики' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 41 WHERE name = 'Соки и воды' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 117, color_id = 92 WHERE name = 'Вейпы и жидкости' AND type = 'system' AND user_id IS NULL;

-- Транспорт
UPDATE categories SET icon_id = 36, color_id = 33 WHERE name = 'Общественный транспорт' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 40, color_id = 37 WHERE name = 'Метро' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 35, color_id = 35 WHERE name = 'Автобус/троллейбус' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 37, color_id = 39 WHERE name = 'Электричка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 38, color_id = 5 WHERE name = 'Такси' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 38 WHERE name = 'Каршеринг' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 41 WHERE name = 'Аренда авто' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 73 WHERE name = 'Топливо (АИ-92)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 75 WHERE name = 'Топливо (АИ-95)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 77 WHERE name = 'Топливо (АИ-98)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 81 WHERE name = 'Топливо (дизель)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 71, color_id = 43 WHERE name = 'Электроавто (зарядка)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 115, color_id = 57 WHERE name = 'Автосервис' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 115, color_id = 63 WHERE name = 'Шиномонтаж' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 115, color_id = 47 WHERE name = 'Мойка автомобиля' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 53 WHERE name = 'Парковка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 39, color_id = 59 WHERE name = 'Платные дороги' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 89, color_id = 1 WHERE name = 'Штрафы ГИБДД' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 25 WHERE name = 'ОСАГО' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 29 WHERE name = 'КАСКО' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 81 WHERE name = 'Транспортный налог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 83 WHERE name = 'Амортизация авто' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 60, color_id = 49 WHERE name = 'Аренда велосипеда' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 39, color_id = 51 WHERE name = 'Аренда самоката' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 38, color_id = 67 WHERE name = 'Такси (в путешествии)' AND type = 'system' AND user_id IS NULL;

-- Жильё
UPDATE categories SET icon_id = 43, color_id = 25 WHERE name = 'Аренда квартиры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 27 WHERE name = 'Аренда комнаты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 29 WHERE name = 'Ипотека' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 91, color_id = 55 WHERE name = 'Коммунальные услуги' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 71, color_id = 57 WHERE name = 'Электричество' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 89, color_id = 59 WHERE name = 'Газ' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 109, color_id = 61 WHERE name = 'Вода' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 89, color_id = 63 WHERE name = 'Отопление' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 89, color_id = 65 WHERE name = 'Вывоз мусора' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 70, color_id = 35 WHERE name = 'Интернет (домашний)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 72, color_id = 33 WHERE name = 'Мобильная связь' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 47, color_id = 67 WHERE name = 'Мебель' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 36 WHERE name = 'Бытовая техника' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 73 WHERE name = 'Ремонт и стройматериалы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 115, color_id = 75 WHERE name = 'Инструменты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 47 WHERE name = 'Уборка (клининг)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 106, color_id = 53 WHERE name = 'Бытовая химия' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 55 WHERE name = 'Химчистка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 57 WHERE name = 'Прачечная' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 49 WHERE name = 'Садоводство' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 72, color_id = 39 WHERE name = 'Умный дом' AND type = 'system' AND user_id IS NULL;

-- Покупки
UPDATE categories SET icon_id = 22, color_id = 9 WHERE name = 'Одежда (мужская)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 13 WHERE name = 'Одежда (женская)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 15 WHERE name = 'Одежда (детская)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 11 WHERE name = 'Обувь (мужская)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 17 WHERE name = 'Обувь (женская)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 19 WHERE name = 'Обувь (детская)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 21 WHERE name = 'Аксессуары (сумки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 66, color_id = 93 WHERE name = 'Аксессуары (часы)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 25 WHERE name = 'Аксессуары (ремни/бижутерия)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 63, color_id = 36 WHERE name = 'Электроника (телефоны)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 40 WHERE name = 'Электроника (ноутбуки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 64, color_id = 42 WHERE name = 'Электроника (планшеты)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 65, color_id = 44 WHERE name = 'Электроника (ТВ)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 68, color_id = 46 WHERE name = 'Электроника (аудио)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 59, color_id = 48 WHERE name = 'Электроника (игровые консоли)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 3 WHERE name = 'Аптека (лекарства)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 48, color_id = 7 WHERE name = 'Аптека (БАДы)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 5 WHERE name = 'Аптека (медизделия)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 47, color_id = 13 WHERE name = 'Косметика (уход)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 15 WHERE name = 'Косметика (декоративная)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 81, color_id = 17 WHERE name = 'Парфюмерия' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 93 WHERE name = 'Ювелирные украшения' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 56, color_id = 51 WHERE name = 'Спорттовары' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 83, color_id = 9 WHERE name = 'Игрушки' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 53 WHERE name = 'Книги (бумажные)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 53, color_id = 55 WHERE name = 'Книги (электронные)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 106, color_id = 57 WHERE name = 'Товары для творчества' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 76, color_id = 61 WHERE name = 'Музыкальные инструменты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 60, color_id = 63 WHERE name = 'Охота и рыбалка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 65 WHERE name = 'Для животных (корма)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 67 WHERE name = 'Для животных (зоотовары)' AND type = 'system' AND user_id IS NULL;

-- Здоровье
UPDATE categories SET icon_id = 45, color_id = 3 WHERE name = 'Терапевт' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 5 WHERE name = 'Педиатр' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 7 WHERE name = 'Хирург' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 9 WHERE name = 'Стоматолог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 11 WHERE name = 'Офтальмолог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 13 WHERE name = 'ЛОР' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 15 WHERE name = 'Гинеколог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 17 WHERE name = 'Уролог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 19 WHERE name = 'Кардиолог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 21 WHERE name = 'Невролог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 23 WHERE name = 'Психиатр/психолог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 25 WHERE name = 'Дерматолог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 27 WHERE name = 'Ортопед' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 29 WHERE name = 'Маммолог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 31 WHERE name = 'Анализы (кровь и т.д.)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 33 WHERE name = 'МРТ/КТ/рентген' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 35 WHERE name = 'УЗИ' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 37 WHERE name = 'Медицинские процедуры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 39 WHERE name = 'Массаж' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 41 WHERE name = 'Мануальная терапия' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 43 WHERE name = 'Иглорефлексотерапия' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 45 WHERE name = 'СПА и салоны красоты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 47 WHERE name = 'Соляная пещера' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 49 WHERE name = 'Фитнес/тренажёрный зал' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 60, color_id = 51 WHERE name = 'Бассейн' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 53 WHERE name = 'Йога/пилатес' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 57, color_id = 55 WHERE name = 'Спортивные секции' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 57 WHERE name = 'Очки и линзы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 59 WHERE name = 'Слуховой аппарат' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 61 WHERE name = 'Медицинская страховка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 63 WHERE name = 'Санаторий' AND type = 'system' AND user_id IS NULL;

-- Развлечения
UPDATE categories SET icon_id = 82, color_id = 1 WHERE name = 'Кино' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 3 WHERE name = 'Театр' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 76, color_id = 5 WHERE name = 'Концерты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 7 WHERE name = 'Цирк' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 76, color_id = 9 WHERE name = 'Музыкальные шоу' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 11 WHERE name = 'Comedy Club' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 13 WHERE name = 'Stand-up' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 15 WHERE name = 'Лекции и meetups' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 17 WHERE name = 'Подписки (Netflix)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 77, color_id = 19 WHERE name = 'Подписки (Spotify)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 77, color_id = 21 WHERE name = 'Подписки (Apple Music)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 23 WHERE name = 'Подписки (YouTube Premium)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 25 WHERE name = 'Подписки (VK/Одноклассники)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 59, color_id = 27 WHERE name = 'Подписки (Игровые)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 29 WHERE name = 'Подписки (Облачные)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 31 WHERE name = 'Подписки (Новостные)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 83, color_id = 33 WHERE name = 'Видеоигры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 63, color_id = 35 WHERE name = 'Мобильные игры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 59, color_id = 37 WHERE name = 'Покупки в играх' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 83, color_id = 39 WHERE name = 'VR-аттракционы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 83, color_id = 41 WHERE name = 'Боулинг' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 83, color_id = 43 WHERE name = 'Бильярд' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 45 WHERE name = 'Настольные игры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 76, color_id = 47 WHERE name = 'Караоке' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 89 WHERE name = 'Казино/ставки' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 91 WHERE name = 'Лотерея' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 102, color_id = 49 WHERE name = 'Парки аттракционов' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 51 WHERE name = 'Зоопарк' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 53 WHERE name = 'Музеи' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 55 WHERE name = 'Выставки' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 57 WHERE name = 'Фестивали' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 59 WHERE name = 'Ночные клубы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 29, color_id = 61 WHERE name = 'Бары' AND type = 'system' AND user_id IS NULL;

-- Образование
UPDATE categories SET icon_id = 52, color_id = 1 WHERE name = 'Школа (кружки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 3 WHERE name = 'Репетиторство (школьное)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 5 WHERE name = 'Репетиторство (ЕГЭ/ОГЭ)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 7 WHERE name = 'Курсы (IT)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 55, color_id = 9 WHERE name = 'Курсы (иностранные языки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 106, color_id = 11 WHERE name = 'Курсы (дизайн)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 13 WHERE name = 'Курсы (маркетинг)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 15 WHERE name = 'Курсы (финансы)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 31, color_id = 17 WHERE name = 'Курсы (кулинария)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 106, color_id = 19 WHERE name = 'Курсы (рукоделие)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 21 WHERE name = 'Курсы (авто)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 23 WHERE name = 'Онлайн-курсы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 25 WHERE name = 'Мастер-классы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 27 WHERE name = 'Профпереподготовка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 29 WHERE name = 'Высшее образование' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 31 WHERE name = 'Аспирантура' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 33 WHERE name = 'Книги (учебные)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 35 WHERE name = 'Канцтовары' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 37 WHERE name = 'Школьная форма' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 39 WHERE name = 'Детский сад' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 41 WHERE name = 'Лагерь (детский)' AND type = 'system' AND user_id IS NULL;

-- Путешествия
UPDATE categories SET icon_id = 37, color_id = 1 WHERE name = 'Авиабилеты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 37, color_id = 3 WHERE name = 'Ж/Д билеты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 35, color_id = 5 WHERE name = 'Автобусные билеты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 37, color_id = 7 WHERE name = 'Паромы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 9 WHERE name = 'Отели (бюджет)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 11 WHERE name = 'Отели (средний)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 13 WHERE name = 'Отели (люкс)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 15 WHERE name = 'Хостелы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 17 WHERE name = 'Апартаменты (Airbnb)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 19 WHERE name = 'Экскурсии' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 21 WHERE name = 'Гиды' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 23 WHERE name = 'Входные билеты (достопримечательности)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 25 WHERE name = 'Страховка путешествий' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 27 WHERE name = 'Аренда авто (в путешествии)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 29 WHERE name = 'Трансферы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 31 WHERE name = 'Сувениры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 33 WHERE name = 'Duty Free' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 26, color_id = 35 WHERE name = 'Питание в путешествии' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 82, color_id = 37 WHERE name = 'Развлечения в путешествии' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 39 WHERE name = 'Виза' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 41 WHERE name = 'Загранпаспорт' AND type = 'system' AND user_id IS NULL;

-- Подарки
UPDATE categories SET icon_id = 85, color_id = 1 WHERE name = 'Подарки (родным)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 3 WHERE name = 'Подарки (друзьям)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 5 WHERE name = 'Подарки (коллегам)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 7 WHERE name = 'Подарки (детям)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 9 WHERE name = 'Цветы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 11 WHERE name = 'Открытки/упаковка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 95, color_id = 13 WHERE name = 'Благотворительность' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 95, color_id = 15 WHERE name = 'Пожертвования (фонды)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 102, color_id = 17 WHERE name = 'Церковь/мечеть/синагога' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 95, color_id = 19 WHERE name = 'Милостыня' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 21 WHERE name = 'Конверты/деньги' AND type = 'system' AND user_id IS NULL;

-- Финансы
UPDATE categories SET icon_id = 88, color_id = 81 WHERE name = 'Банковская комиссия' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 83 WHERE name = 'Перевод (СБП)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 85 WHERE name = 'Перевод (банковский)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 87 WHERE name = 'Обналичивание' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 89 WHERE name = 'Инвестиции (покупка акций)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 91 WHERE name = 'Инвестиции (покупка облигаций)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 93 WHERE name = 'Криптовалюта' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 95 WHERE name = 'НДФЛ' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 97 WHERE name = 'Транспортный налог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 99 WHERE name = 'Земельный налог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 101 WHERE name = 'Имущественный налог' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 1 WHERE name = 'Штрафы (не ГИБДД)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 103 WHERE name = 'Пошлины' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 105 WHERE name = 'Оплата пошлин' AND type = 'system' AND user_id IS NULL;

-- Услуги
UPDATE categories SET icon_id = 24, color_id = 1 WHERE name = 'Почта/доставка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 3 WHERE name = 'Нотариус' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 5 WHERE name = 'Юрист' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 7 WHERE name = 'Адвокат' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 9 WHERE name = 'Риелтор' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 11 WHERE name = 'Оценка имущества' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 13 WHERE name = 'Грузчики' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 15 WHERE name = 'Клининг' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 76, color_id = 17 WHERE name = 'Репетитор (музыка)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 19 WHERE name = 'Няня' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 21 WHERE name = 'Сиделка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 23 WHERE name = 'Страхование (жизнь)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 25 WHERE name = 'Страхование (имущество)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 27 WHERE name = 'Страхование (ОСАГО)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 29 WHERE name = 'Страхование (КАСКО)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 31 WHERE name = 'Страхование (ДМС)' AND type = 'system' AND user_id IS NULL;

-- Бизнес
UPDATE categories SET icon_id = 112, color_id = 1 WHERE name = 'Офисные расходы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 3 WHERE name = 'Расходные материалы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 115, color_id = 5 WHERE name = 'Инструменты (проф.)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 22, color_id = 7 WHERE name = 'Спецодежда' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 9 WHERE name = 'Корпоративные подарки' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 11 WHERE name = 'Командировочные' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 13 WHERE name = 'Конференции' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 26, color_id = 15 WHERE name = 'Бизнес-ланч' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 17 WHERE name = 'Программное обеспечение' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 70, color_id = 19 WHERE name = 'Хостинг/домены' AND type = 'system' AND user_id IS NULL;

-- Животные
UPDATE categories SET icon_id = 104, color_id = 1 WHERE name = 'Корм для собак' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 3 WHERE name = 'Корм для кошек' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 5 WHERE name = 'Корм для птиц' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 7 WHERE name = 'Корм для рыбок' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 9 WHERE name = 'Корм для грызунов' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 11 WHERE name = 'Ветеринар (кошки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 13 WHERE name = 'Ветеринар (собаки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 15 WHERE name = 'Ветеринар (другие)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 17 WHERE name = 'Вакцинация' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 19 WHERE name = 'Стерилизация' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 21 WHERE name = 'Зоотовары (игрушки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 23 WHERE name = 'Зоотовары (одежда)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 25 WHERE name = 'Зоотовары (переноски)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 104, color_id = 27 WHERE name = 'Зоотовары (миски/лежанки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 29 WHERE name = 'Груминг (собаки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 31 WHERE name = 'Груминг (кошки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 44, color_id = 33 WHERE name = 'Зоогостиница' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 115, color_id = 35 WHERE name = 'Дрессировка' AND type = 'system' AND user_id IS NULL;

-- Саморазвитие
UPDATE categories SET icon_id = 49, color_id = 1 WHERE name = 'Коучинг' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 3 WHERE name = 'Тренинги' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 5 WHERE name = 'Тета-исцеление' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 46, color_id = 7 WHERE name = 'Медитации' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 9 WHERE name = 'Консультации (психолог)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 11 WHERE name = 'Консультации (финансовый советник)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 114, color_id = 13 WHERE name = 'Консультации (юрист)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 15 WHERE name = 'Астрология/нумерология' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 49, color_id = 17 WHERE name = 'Эзотерика' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 19 WHERE name = 'Антикварные товары' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 21 WHERE name = 'Коллекционирование' AND type = 'system' AND user_id IS NULL;

-- Прочее расходы
UPDATE categories SET icon_id = 119, color_id = 81 WHERE name = 'Непредвиденные расходы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 83 WHERE name = 'Резервный фонд' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 119, color_id = 85 WHERE name = 'Неизвестные расходы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 87 WHERE name = 'Наличные (неизвестно куда)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 89 WHERE name = 'Долги (возврат)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 91 WHERE name = 'Алименты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 119, color_id = 93 WHERE name = 'Прочие расходы' AND type = 'system' AND user_id IS NULL;

-- ============================================
-- INCOME CATEGORIES
-- ============================================

-- Трудоустройство
UPDATE categories SET icon_id = 112, color_id = 49 WHERE name = 'Зарплата (чистыми)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 51 WHERE name = 'Зарплата (gross)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 53 WHERE name = 'Аванс' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 55 WHERE name = 'Зарплата (13-я)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 57 WHERE name = 'Зарплата (премия)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 59 WHERE name = 'Зарплата (квартальная)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 61 WHERE name = 'Зарплата (годовая)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 63 WHERE name = 'Бонус за производительность' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 65 WHERE name = 'Overtime' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 67 WHERE name = 'Комиссионные' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 69 WHERE name = 'Чаевые' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 113, color_id = 71 WHERE name = 'Фриланс (разовый)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 113, color_id = 73 WHERE name = 'Фриланс (постоянный)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 113, color_id = 75 WHERE name = 'Подработка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 77 WHERE name = 'Работа в выходные' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 79 WHERE name = 'Удалённая работа' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 81 WHERE name = 'Вознаграждение за рекомендации' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 83 WHERE name = 'Компенсация за питание' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 85 WHERE name = 'Компенсация за транспорт' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 87 WHERE name = 'Компенсация за связь' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 89 WHERE name = 'Компенсация за жильё' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 91 WHERE name = 'Путешествия (оплачиваемые)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 93 WHERE name = 'Обучение (оплачиваемое)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 95 WHERE name = 'Медицинская страховка (от раб.)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 97 WHERE name = 'Пенсия (от работодателя)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 99 WHERE name = 'Дивиденды (от раб.)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 101 WHERE name = 'Выходное пособие' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 103 WHERE name = 'Отпускные' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 105 WHERE name = 'Больничный' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 107 WHERE name = 'Декретные' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 109 WHERE name = 'Пособие по безработице' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 111 WHERE name = 'Стипендия' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 113 WHERE name = 'Гранты' AND type = 'system' AND user_id IS NULL;

-- Инвестиции
UPDATE categories SET icon_id = 88, color_id = 49 WHERE name = 'Дивиденды (акции)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 51 WHERE name = 'Купоны (облигации)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 53 WHERE name = 'Проценты (вклады)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 55 WHERE name = 'Проценты (кэшбэк на вклад)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 57 WHERE name = 'Продажа акций' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 59 WHERE name = 'Продажа облигаций' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 61 WHERE name = 'Продажа ETF' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 63 WHERE name = 'Продажа криптовалюты' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 65 WHERE name = 'Продажа недвижимости' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 67 WHERE name = 'Продажа авто' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 69 WHERE name = 'Продажа вещей' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 71 WHERE name = 'Доход от аренды (квартира)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 73 WHERE name = 'Доход от аренды (комната)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 75 WHERE name = 'Доход от аренды (авто)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 77 WHERE name = 'Доход от аренды (техника)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 79 WHERE name = 'Роялти (авторские)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 81 WHERE name = 'Роялти (патенты)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 83 WHERE name = 'Патентные отчисления' AND type = 'system' AND user_id IS NULL;

-- Имущество
UPDATE categories SET icon_id = 45, color_id = 49 WHERE name = 'Продажа квартиры' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 51 WHERE name = 'Продажа дома' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 102, color_id = 53 WHERE name = 'Продажа земли' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 55 WHERE name = 'Продажа гаража' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 57 WHERE name = 'Продажа дачи' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 59 WHERE name = 'Продажа машины' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 61 WHERE name = 'Продажа мотоцикла' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 63 WHERE name = 'Продажа велосипеда' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 65 WHERE name = 'Продажа техники' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 47, color_id = 67 WHERE name = 'Продажа мебели' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 69 WHERE name = 'Продажа антиквариата' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 71 WHERE name = 'Продажа коллекций' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 73 WHERE name = 'Сдача в аренду (квартира)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 75 WHERE name = 'Сдача в аренду (комната)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 34, color_id = 77 WHERE name = 'Сдача в аренду (парковка)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 47, color_id = 79 WHERE name = 'Сдача в аренду (кладовка)' AND type = 'system' AND user_id IS NULL;

-- Подарки и выигрыши
UPDATE categories SET icon_id = 85, color_id = 49 WHERE name = 'Деньги в подарок (НГ)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 51 WHERE name = 'Деньги в подарок (день рождения)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 53 WHERE name = 'Деньги в подарок (свадьба)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 55 WHERE name = 'Деньги в подарок (без события)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 57 WHERE name = 'Выигрыш (лотерея)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 59 WHERE name = 'Выигрыш (казино)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 61 WHERE name = 'Выигрыш (ставки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 63 WHERE name = 'Приз (конкурс)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 65 WHERE name = 'Приз (розыгрыш)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 67 WHERE name = 'Наследство' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 69 WHERE name = 'Завещание' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 71 WHERE name = 'Страховая выплата (жизнь)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 73 WHERE name = 'Страховая выплата (здоровье)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 75 WHERE name = 'Страховая выплата (имущество)' AND type = 'system' AND user_id IS NULL;

-- Кэшбэк и бонусы
UPDATE categories SET icon_id = 90, color_id = 49 WHERE name = 'Кэшбэк (банковский)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 51 WHERE name = 'Кэшбэк (покупки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 53 WHERE name = 'Кэшбэк (сервисы)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 37, color_id = 55 WHERE name = 'Бонусы (авиамили)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 37, color_id = 57 WHERE name = 'Бонусы (ж/д)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 59 WHERE name = 'Бонусы (отели)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 61 WHERE name = 'Бонусы (магазины)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 37, color_id = 63 WHERE name = 'Мили (авиакомпании)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 65 WHERE name = 'Баллы (аптеки)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 67 WHERE name = 'Промокоды (возврат)' AND type = 'system' AND user_id IS NULL;

-- Возвраты
UPDATE categories SET icon_id = 85, color_id = 49 WHERE name = 'Возврат товара' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 51 WHERE name = 'Возврат услуги' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 53 WHERE name = 'Возврат налога (НДФЛ)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 55 WHERE name = 'Возврат налога (имущественный)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 57 WHERE name = 'Возврат страховки' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 45, color_id = 59 WHERE name = 'Возврат депозита (аренда)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 61 WHERE name = 'Возврат залога' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 63 WHERE name = 'Компенсация (судебная)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 112, color_id = 65 WHERE name = 'Компенсация (от работодателя)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 67 WHERE name = 'Компенсация (от государства)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 69 WHERE name = 'Алименты (получено)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 71 WHERE name = 'Пособие на ребёнка' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 73 WHERE name = 'Материнский капитал' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 75 WHERE name = 'Единовременная выплата' AND type = 'system' AND user_id IS NULL;

-- Интернет доход
UPDATE categories SET icon_id = 84, color_id = 49 WHERE name = 'Продажа фото' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 51 WHERE name = 'Продажа видео' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 76, color_id = 53 WHERE name = 'Продажа музыки' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 55 WHERE name = 'Продажа текстов' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 54, color_id = 57 WHERE name = 'Продажа ПО' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 59, color_id = 59 WHERE name = 'Продажа игр' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 70, color_id = 61 WHERE name = 'Доход с блога (реклама)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 63 WHERE name = 'Доход с YouTube' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 65 WHERE name = 'Доход с Telegram' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 67 WHERE name = 'Доход с TikTok' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 84, color_id = 69 WHERE name = 'Доход с Instagram' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 24, color_id = 71 WHERE name = 'Доход с маркетплейсов' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 73 WHERE name = 'Партнёрские программы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 75 WHERE name = 'Реферальные бонусы' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 52, color_id = 77 WHERE name = 'Опросы (оплата)' AND type = 'system' AND user_id IS NULL;

-- Прочее доходы
UPDATE categories SET icon_id = 85, color_id = 81 WHERE name = 'Неизвестный доход' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 83 WHERE name = 'Найденные деньги' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 88, color_id = 85 WHERE name = 'Долг (полученный возврат)' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 90, color_id = 87 WHERE name = 'Перевод от другого счёта' AND type = 'system' AND user_id IS NULL;
UPDATE categories SET icon_id = 85, color_id = 89 WHERE name = 'Прочее' AND type = 'system' AND user_id IS NULL;
