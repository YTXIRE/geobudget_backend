-- Icons tables
CREATE TABLE icon_groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE icons (
    id SERIAL PRIMARY KEY,
    group_id INT NOT NULL REFERENCES icon_groups(id),
    name VARCHAR(255) NOT NULL,
    code_point BIGINT NOT NULL
);

-- Insert icon groups and icons
INSERT INTO icon_groups (name) VALUES
('Покупки'), ('Еда'), ('Транспорт'), ('Дом'), ('Здоровье'),
('Образование'), ('Спорт'), ('Техника'), ('Связь'), ('Музыка'),
('Развлечения'), ('Финансы'), ('Безопасность'), ('Природа'), ('Творчество'), ('Работа'), ('Другое');

-- Покупки (group 1)
INSERT INTO icons (group_id, name, code_point) VALUES
(1, 'shopping_cart', 59321), (1, 'shopping_bag', 59320), (1, 'store', 59553),
(1, 'storefront', 59622), (1, 'local_grocery_store', 59146), (1, 'point_of_sale', 59368),
(1, 'inventory', 58983), (1, 'credit_card', 58287), (1, 'attach_money', 57853), (1, 'money', 59184);

-- Еда (group 2)
INSERT INTO icons (group_id, name, code_point) VALUES
(2, 'restaurant', 59473), (2, 'fastfood', 58864), (2, 'local_cafe', 59036),
(2, 'coffee', 58142), (2, 'icecream', 58974), (2, 'cake', 58022),
(2, 'kitchen', 59061), (2, 'lunch_dining', 60048), (2, 'dinner_dining', 60049), (2, 'ramen_dining', 60052);

-- Транспорт (group 3)
INSERT INTO icons (group_id, name, code_point) VALUES
(3, 'directions_car', 58465), (3, 'directions_bus', 58455), (3, 'local_taxi', 59086),
(3, 'flight', 58884), (3, 'train', 59936), (3, 'commute', 59662),
(3, 'directions_railway', 58464), (3, 'directions_transit', 58470), (3, 'subway', 59946), (3, 'tram', 59937);

-- Дом (group 4)
INSERT INTO icons (group_id, name, code_point) VALUES
(4, 'home', 58871), (4, 'house', 58927), (4, 'cottage', 59680),
(4, 'apartment', 57669), (4, 'real_estate_agent', 59371), (4, 'garage', 58820),
(4, 'bed', 58002), (4, 'bathroom', 59706), (4, 'bathtub', 57968), (4, 'countertops', 59676);

-- Здоровье (group 5)
INSERT INTO icons (group_id, name, code_point) VALUES
(5, 'medical_services', 58954), (5, 'fitness_center', 58785), (5, 'spa', 59522),
(5, 'face', 58781), (5, 'healing', 58867), (5, 'health_and_safety', 58868),
(5, 'masks', 58951), (5, 'local_hospital', 59055), (5, 'psychology', 59354), (5, 'medication', 58955);

-- Образование (group 6)
INSERT INTO icons (group_id, name, code_point) VALUES
(6, 'school', 59491), (6, 'book', 58010), (6, 'library_books', 59002),
(6, 'auto_stories', 59657), (6, 'menu_book', 59004), (6, 'laptop', 59077),
(6, 'computer', 58194), (6, 'science', 59496), (6, 'calculate', 58021), (6, 'cast_for_education', 59665);

-- Спорт (group 7)
INSERT INTO icons (group_id, name, code_point) VALUES
(7, 'sports_soccer', 59534), (7, 'sports_basketball', 59527), (7, 'sports_tennis', 59539),
(7, 'sports_football', 59530), (7, 'sports_baseball', 59526), (7, 'sports_esports', 59541),
(7, 'pool', 59342), (7, 'hiking', 58916), (7, 'kayaking', 58922), (7, 'rowing', 59379);

-- Техника (group 8)
INSERT INTO icons (group_id, name, code_point) VALUES
(8, 'phone_android', 59330), (8, 'smartphone', 59516), (8, 'tablet', 59558),
(8, 'laptop', 59077), (8, 'computer', 58194), (8, 'tv', 59976),
(8, 'watch', 60041), (8, 'headphones', 58868), (8, 'camera_alt', 58027), (8, 'mic', 59171);

-- Связь (group 9)
INSERT INTO icons (group_id, name, code_point) VALUES
(9, 'wifi', 60044), (9, 'bluetooth', 58012), (9, 'settings', 59498),
(9, 'usb', 60012), (9, 'battery_full', 57870), (9, 'signal_cellular_alt', 59509),
(9, 'network_wifi', 59030), (9, 'developer_mode', 58449), (9, 'power', 59342), (9, 'memory', 59011);

-- Музыка (group 10)
INSERT INTO icons (group_id, name, code_point) VALUES
(10, 'music_note', 59193), (10, 'headphones', 58868), (10, 'mic', 59171),
(10, 'album', 57662), (10, 'radio', 59367), (10, 'speaker', 59525),
(10, 'queue_music', 59366), (10, 'audiotrack', 57855), (10, 'play_arrow', 59328), (10, 'pause', 59324);

-- Развлечения (group 11)
INSERT INTO icons (group_id, name, code_point) VALUES
(11, 'movie', 59190), (11, 'sports_esports', 59541), (11, 'games', 58821),
(11, 'grid_view', 58842), (11, 'apps', 57850), (11, 'category', 58035),
(11, 'video_library', 60030), (11, 'photo_library', 59335), (11, 'image', 58976), (11, 'photo', 59334);

-- Финансы (group 12)
INSERT INTO icons (group_id, name, code_point) VALUES
(12, 'savings', 59484), (12, 'account_balance', 57661), (12, 'payments', 59326),
(12, 'receipt_long', 59370), (12, 'credit_score', 58952), (12, 'money', 59184),
(12, 'attach_money', 57853), (12, 'currency_exchange', 58290), (12, 'money_off', 59185), (12, 'price_change', 59349);

-- Безопасность (group 13)
INSERT INTO icons (group_id, name, code_point) VALUES
(13, 'security', 59497), (13, 'shield', 59506), (13, 'lock', 59080),
(13, 'lock_open', 59081), (13, 'verified_user', 60029), (13, 'privacy_tip', 59352),
(13, 'policy', 59344), (13, 'gpp_good', 58901), (13, 'local_police', 59063), (13, 'local_fire_department', 59049);

-- Природа (group 14)
INSERT INTO icons (group_id, name, code_point) VALUES
(14, 'park', 59322), (14, 'forest', 58817), (14, 'terrain', 59922),
(14, 'pets', 59328), (14, 'emoji_nature', 58776), (14, 'eco', 58769),
(14, 'local_florist', 59052), (14, 'nature', 59196), (14, 'water_drop', 60038), (14, 'grass', 58840);

-- Творчество (group 15)
INSERT INTO icons (group_id, name, code_point) VALUES
(15, 'palette', 59321), (15, 'brush', 58016), (15, 'edit', 57973),
(15, 'create', 58286), (15, 'draw', 58864), (15, 'format_paint', 58896),
(15, 'photo_camera', 59334), (15, 'camera_alt', 58027), (15, 'filter', 58784), (15, 'crop', 58287);

-- Работа (group 16)
INSERT INTO icons (group_id, name, code_point) VALUES
(16, 'work', 60046), (16, 'business', 58020), (16, 'business_center', 58019),
(16, 'domain', 58418), (16, 'people', 59329), (16, 'group', 58843),
(16, 'engineering', 58774), (16, 'handyman', 58911), (16, 'build', 58017), (16, 'construction', 58268);

-- Другое (group 17)
INSERT INTO icons (group_id, name, code_point) VALUES
(17, 'star', 59539), (17, 'favorite', 58785), (17, 'thumb_up', 59929),
(17, 'emoji_emotions', 58777), (17, 'lightbulb', 59079), (17, 'bolt', 58015),
(17, 'more_vert', 59189), (17, 'more_horiz', 59188), (17, 'add', 57689), (17, 'check', 58106);
