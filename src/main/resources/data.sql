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
INSERT IGNORE INTO stores (id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, capacity) VALUES (1, 'なごみ処　晴', 'wasyoku.jpg', '創業50年以上の老舗。コースでのご利用もいただけます。（要予約：4人以上）', '11:00:00', '19:00:00', 3000, 5000, '城木町XX-XX', '052-001-001', 50);
INSERT IGNORE INTO stores (id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, capacity) VALUES (2, 'NAGOYA WEST', 'yosyoku.jpg', '西洋風料理専門店です。おススメは赤牛のヒレステーキ。', '10:00:00', '22:00:00', 4000, 6000, '名古屋市瑞穂区XX-XX', '052-002-002', 30);
INSERT IGNORE INTO stores (id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, capacity) VALUES (3, '中華料理屋シェンロン','chuka.jpg', '本場で修行を重ねた店主が腕によりをかけ本場に近い味をお届けします。', '15:00:00', '23:00:00', 2500,5000, '名古屋市豊山町XX-XX', '052-003-003', 100);
INSERT IGNORE INTO stores (id, name, image_name, description, start_time, end_time, price_min, price_max, address, phone_number, capacity) VALUES (4, '魚劉-gyoryu-', 'kaisen.jpg', '初代直伝秘伝のタレで新鮮なお刺身をご堪能ください。', '10:00:00', '21:00:00', 3000, 5000, '港区XX-XX', '052-004-004', 15);

/* holidaysテーブル */
INSERT IGNORE INTO holidays (id, day, day_index) VALUES
(1, '月', 1),
(2, '火', 2),
(3, '水', 3),
(4, '木', 4),
(5, '金', 5),
(6, '土', 6),
(7, '日', 7),
(8, '不定休', null);

/* holiday_storeテーブル */
INSERT IGNORE INTO holiday_store (id, store_id, holiday_id) VALUES
(1, 1, 1),
(2, 1, 3),
(3, 3, 8),
(4, 4, 2);

/* reservationsテーブル */
INSERT IGNORE INTO reservations (id, store_id, user_id, reserved_datetime, number_of_people) VALUES (1, 1, 1, '2025-03-01 12:00:00', 3);

/* reviewsテーブル */
INSERT IGNORE INTO reviews (id, content, score, store_id, user_id) VALUES
(1, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 4, 1, 1),
(2, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 3, 2, 5),
(3, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 3, 3, 4),
(4, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 2, 4, 1),
(5, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 5, 1, 5),
(6, 'ちょっとリッチに和食コースで利用しました。畳の個室でゆっくりと食事を楽しめます。', 3, 5, 4);

/* companiesテーブル */
INSERT IGNORE INTO companies (id, name, address, representative, establishment_date, capital, business, number_of_employees) VALUES
(1, 'NAGOYAMESHI株式会社', '東京都千代田区神田練堀町300番地　住友不動産秋葉原駅前ビル5F', '佐藤　三郎', '2000年4月15日', '110,000千円', '飲食店等の情報提供サービス', '100名');

/* termsテーブル */
INSERT IGNORE INTO terms (id, content) VALUES
(1, '<p>この利用規約（以下、「本規約」といいます。）は、NAGOYAMESHI株式会社（以下、「当社」といいます。）がこのウェブサイト上で提供するサービス（以下、「本サービス」といいます。）の利用条件を定めるものです。登録ユーザーの皆さま（以下、「ユーザー」といいます。）には、本規約に従って、本サービスをご利用いただきます。</p>');

/* favoritesテーブル */
INSERT IGNORE INTO favorites (id, store_id, user_id) VALUES
(1,5,2),
(2,3,1),
(3,6,2),
(4,4,1),
(5,5,2);

