-- Colors tables
CREATE TABLE color_groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE colors (
    id SERIAL PRIMARY KEY,
    group_id INT NOT NULL REFERENCES color_groups(id),
    name VARCHAR(255) NOT NULL,
    hex VARCHAR(7) NOT NULL,
    argb BIGINT NOT NULL
);

-- Insert color groups and colors
INSERT INTO color_groups (name) VALUES
('Красные'), ('Розовые'), ('Фиолетовые'), ('Синие'), ('Голубые'),
('Бирюзовые'), ('Зелёные'), ('Жёлтые'), ('Оранжевые'), ('Коричневые'),
('Серые'), ('Яркие'), ('Пастельные'), ('Неоновые'), ('Тёмные'), ('Природные');

-- Красные (group 1)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(1, 'Red 600', 'E53935', 4294924341), (1, 'Red 700', 'D32F2F', 4291613231),
(1, 'Red 800', 'C62828', 4288420904), (1, 'Red 900', 'B71C1C', 4285058348),
(1, 'Red 400', 'EF5350', 4294936784), (1, 'Red 300', 'E57373', 4294928243),
(1, 'Red 200', 'EF9A9A', 4294937578), (1, 'Red 100', 'FFCDD2', 4294946420);

-- Розовые (group 2)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(2, 'Pink 600', 'D81B60', 4293467488), (2, 'Pink 700', 'C2185B', 4291646043),
(2, 'Pink 800', 'AD1457', 4288820055), (2, 'Pink 900', '880E4F', 4286013775),
(2, 'Pink 400', 'EC407A', 4293466490), (2, 'Pink 300', 'F06292', 4293468982),
(2, 'Pink 200', 'F48FB1', 4293469969), (2, 'Pink 100', 'F8BBD9', 4293471452);

-- Фиолетовые (group 3)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(3, 'Purple 600', '8E24AA', 4291650730), (3, 'Purple 700', '7B1FA2', 4289750930),
(3, 'Purple 800', '6A1B9A', 4287851130), (3, 'Purple 900', '4A148C', 4283235708),
(3, 'Purple 400', 'AB47BC', 4291652156), (3, 'Purple 300', 'BA68C8', 4291652972),
(3, 'Purple 200', 'CE93D8', 4291653900), (3, 'Purple 100', 'E1BEE7', 4291655052);

-- Синие (group 4)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(4, 'Indigo 600', '3949AB', 4284770971), (4, 'Indigo 700', '303F9F', 4282034719),
(4, 'Indigo 800', '283593', 4279298463), (4, 'Indigo 900', '1A237E', 4276306046),
(4, 'Indigo 400', '5C6BC0', 4284772496), (4, 'Indigo 300', '7986CB', 4284774251),
(4, 'Indigo 200', '9FA8DA', 4284776006), (4, 'Indigo 100', 'C5CAE9', 4284778289);

-- Голубые (group 5)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(5, 'Blue 600', '1E88E5', 4280390685), (5, 'Blue 700', '1976D2', 4278192930),
(5, 'Blue 800', '1565C0', 4276075300), (5, 'Blue 900', '0D47A1', 4271182625),
(5, 'Blue 400', '42A5F5', 4280392533), (5, 'Blue 300', '64B5F6', 4280393437),
(5, 'Blue 200', '90CAF9', 4280394341), (5, 'Blue 100', 'BBDEFB', 4280395773);

-- Бирюзовые (group 6)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(6, 'Cyan 600', '00ACC1', 4278221761), (6, 'Cyan 700', '00838F', 4276053903),
(6, 'Cyan 800', '006064', 4273886045), (6, 'Cyan 400', '26C6DA', 4278223946),
(6, 'Cyan 300', '4DD0E1', 4278225104), (6, 'Cyan 200', '80DEEA', 4278226514),
(6, 'Cyan 100', 'B2EBF2', 4278228756), (6, 'Teal 100', 'B2DFDB', 4280155483);

-- Зелёные (group 7)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(7, 'Green 600', '43A047', 4283215695), (7, 'Green 700', '388E3C', 4280712540),
(7, 'Green 800', '2E7D32', 4278209380), (7, 'Green 900', '1B5E20', 4275703040),
(7, 'Green 400', '9CCC65', 4283217829), (7, 'Green 300', 'AED581', 4283218873),
(7, 'Green 200', 'C5E1A5', 4283220237), (7, 'Green 100', 'DCEDC8', 4283222412);

-- Жёлтые (group 8)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(8, 'Yellow 600', 'FDD835', 4294315061), (8, 'Yellow 700', 'FBC02D', 4292012589),
(8, 'Yellow 800', 'F9A825', 4289710117), (8, 'Yellow 900', 'F57F17', 4286145015),
(8, 'Yellow 400', 'FFEE58', 4294316104), (8, 'Yellow 300', 'FFF176', 4294316840),
(8, 'Yellow 200', 'FFF59D', 4294317576), (8, 'Yellow 100', 'FFFDE7', 4294319096);

-- Оранжевые (group 9)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(9, 'Orange 600', 'FB8C00', 4288338688), (9, 'Orange 700', 'F57C00', 4286036224),
(9, 'Orange 800', 'EF6C00', 4283733760), (9, 'Orange 900', 'E65100', 4279415040),
(9, 'Orange 400', 'FFA726', 4288340280), (9, 'Orange 300', 'FFB74D', 4288341380),
(9, 'Orange 200', 'FFCC80', 4288342868), (9, 'Orange 100', 'FFE0B2', 4288345132);

-- Коричневые (group 10)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(10, 'Brown 600', '6D4C41', 4282334529), (10, 'Brown 700', '5D4037', 4279728823),
(10, 'Brown 800', '4E342E', 4277123117), (10, 'Brown 900', '3E2723', 4274517411),
(10, 'Brown 400', 'A1887F', 4282336849), (10, 'Brown 300', 'BCAAA4', 4282338228),
(10, 'Brown 200', 'D7CCC8', 4282340245), (10, 'Brown 100', 'EFEBE9', 4282343960);

-- Серые (group 11)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(11, 'Grey 600', '757575', 4281772549), (11, 'Grey 700', '616161', 4279165037),
(11, 'Grey 800', '424242', 4276557525), (11, 'Grey 900', '212121', 4273947901),
(11, 'Grey 400', 'BDBDBD', 4281775613), (11, 'Grey 300', '9E9E9E', 4281773981),
(11, 'Grey 200', 'E0E0E0', 4281780717), (11, 'Grey 100', 'F5F5F5', 4281788261);

-- Яркие (group 12)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(12, 'Red Accent', 'FF1744', 4294921028), (12, 'Purple Accent', 'D500F9', 4293462777),
(12, 'Blue Accent', '2979FF', 4285116671), (12, 'Green Accent', '00E676', 4278458902),
(12, 'Yellow Accent', 'FFEA00', 4294318880), (12, 'Orange Accent', 'FF6D00', 4286333440),
(12, 'Cyan Accent', '00B8D4', 4278222292), (12, 'Light Green Accent', '76FF03', 4290638595);

-- Пастельные (group 13)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(13, 'Rose', 'FFB3BA', 4294947258), (13, 'Peach', 'FFDFBA', 4294952122),
(13, 'Lemon', 'FFFFBA', 4294966202), (13, 'Mint', 'BAFFBA', 4286619898),
(13, 'Sky', 'BAFFFF', 4289394815), (13, 'Lavender', 'BABAFF', 4294932991),
(13, 'Pink', 'FFBAFF', 4294949887), (13, 'Light Pink', 'FFBAFF', 4294949887);

-- Неоновые (group 14)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(14, 'Magenta', 'FF00FF', 4294902015), (14, 'Cyan', '00FFFF', 4278255615),
(14, 'Lime', '00FF00', 4278255616), (14, 'Yellow', 'FFFF00', 4294944000),
(14, 'Hot Pink', 'FF0080', 4294920320), (14, 'Electric Purple', '8000FF', 4286578687),
(14, 'Teal', '008080', 4278222976), (14, 'Purple', '800080', 4286578688);

-- Тёмные (group 15)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(15, 'Dark Blue 1', '1A1A2E', 4279359022), (15, 'Dark Blue 2', '16213E', 4277072446),
(15, 'Dark Blue 3', '0F3460', 4273476704), (15, 'Dark Blue 4', '1F4068', 4278079072),
(15, 'Dark Grey 1', '2C3338', 4279440664), (15, 'Dark Grey 2', '395B64', 4279767908),
(15, 'Dark Green 1', '3C4A3F', 4279518271), (15, 'Dark Green 2', '4A5D4E', 4279851102);

-- Природные (group 16)
INSERT INTO colors (group_id, name, hex, argb) VALUES
(16, 'Saddle Brown', '8B4513', 4280720131), (16, 'Sienna', 'A0522D', 4282522669),
(16, 'Peru', 'CD853F', 4285018431), (16, 'Chocolate', 'D2691E', 4285728698),
(16, 'Burlywood', 'DEB887', 4287531975), (16, 'Wheat', 'F5DEB3', 4289447091),
(16, 'Light Salmon', 'FFA07A', 4293372538), (16, 'Salmon', 'FA8072', 4293370098);
