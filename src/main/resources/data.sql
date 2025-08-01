/* rolesテーブル  
	1 : 無料会員
	2 : 有料会員
	3 : 管理者*/
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_SUBSCRIBER');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'ROLE_ADMIN');

/* usersテーブル */
INSERT IGNORE INTO users (id, name, furigana, phone_number, email, password, role_id, enabled) VALUES (1, '田中　太郎', 'タナカ　タロウ', '012-345-678', 'taro.tanaka@example.com', '$2a$10$Ac.NsfqD/sEW1/8BlBvKb.QwPTsCzIF37eSEg.hSo8KyuUsG5ZnxG', 1, false);
INSERT IGNORE INTO users (id, name, furigana, phone_number, email, password, role_id, enabled) VALUES (2, '鈴木　次郎', 'スズキ　ジロウ', '123-456-789', 'jiro.suzuki@example.com', 'password', 2, true);
INSERT IGNORE INTO users (id, name, furigana, phone_number, email, password, role_id, enabled) VALUES (3, '佐藤　三郎', 'サトウ　サブロウ', '234-567-891', 'saburo.sato@example.com', 'password', 3, true);

/* categoriesテーブル */
INSERT IGNORE INTO categories (id, name) VALUES (1, '和食');
INSERT IGNORE INTO categories (id, name) VALUES (2, '洋食');
INSERT IGNORE INTO categories (id, name) VALUES (3, '中華');
INSERT IGNORE INTO categories (id, name) VALUES (4, '魚介・海鮮料理');
INSERT IGNORE INTO categories (id, name) VALUES (5, 'パスタ');
INSERT IGNORE INTO categories (id, name) VALUES (6, 'ピザ');
INSERT IGNORE INTO categories (id, name) VALUES (7, 'パン');
INSERT IGNORE INTO categories (id, name) VALUES (8, 'ラーメン');
INSERT IGNORE INTO categories (id, name) VALUES (9, 'そば・うどん');

/* storesテーブル */
INSERT IGNORE INTO stores (id, category_id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, holidays, capacity) VALUES (1, 1, 'なごみ処　晴', 'wasyoku.jpg', '創業50年以上の老舗。コースでのご利用もいただけます。（要予約：4人以上）', '11:00:00', '19:00:00', 3000, 5000, '城木町XX-XX', '052-001-001', '日', 50);
INSERT IGNORE INTO stores (id, category_id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, holidays, capacity) VALUES (2, 2, 'NAGOYA WEST', 'yosyoku.jpg', '西洋風料理専門店です。おススメは赤牛のヒレステーキ。', '10:00:00', '22:00:00', 4000, 6000, '名古屋市瑞穂区XX-XX', '052-002-002', '月', 30);
INSERT IGNORE INTO stores (id, category_id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, holidays, capacity) VALUES (3, 3, '中華料理屋シェンロン','chuka.jpg', '本場で修行を重ねた店主が腕によりをかけ本場に近い味をお届けします。', '15:00:00', '23:00:00', 2500,5000, '名古屋市豊山町XX-XX', '052-003-003', '木', 100);
INSERT IGNORE INTO stores (id, category_id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, holidays, capacity) VALUES (4, 4, '魚劉-gyoryu-', 'kaisen.jpg', '初代直伝秘伝のタレで新鮮なお刺身をご堪能ください。', '10:00:00', '21:00:00', 3000, 5000, '港区XX-XX', '052-004-004', '月', 15);

/* reservationsテーブル */
INSERT IGNORE INTO reservations (id, store_id, user_id, reserved_datetime, number_of_people) VALUES (1, 1, 1, '2025-03-01 12:00:00', 3);

/* reviewsテーブル */
INSERT IGNORE INTO reviews (id, content, score, store_id, user_id) VALUES (1, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 3, 1, 4);
