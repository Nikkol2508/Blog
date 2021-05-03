INSERT INTO users (is_moderator, reg_time, name, email, password)
VALUES (1, 1619945304195, 'Nikolai', 'fdc@shdgf.ru', 'ksjhdgskd'),
(1, 1619945305195, 'Valera', 'vsdgc@shdgf.ru', 'ksjhdgskdjhgkj');

INSERT INTO posts (is_active, moderator_id, user_id, time, title, text, view_count)
VALUES (1, 1, 1, 1619945304247, 'Первый пост', 'Текст первого поста', 1),
(1, 1, 2, 1619945305247, 'Второй пост', 'Текст второго поста', 1);

INSERT INTO post_voters (user_id, post_id, time, value)
VALUES (2, 1, 1619945305347, 1);

INSERT INTO tags (name) VALUES ('Первый тэг'), ('Второй тэг');

INSERT INTO post_comments (post_id, user_id, time, text)
VALUES (1, 1, 1619945304286, 'Текст комментария к первому посту');

INSERT INTO global_settings (code, name, value)
VALUES ('MULTIUSER_MODE', 'Многопользовательский режим', 'YES'),
('POST_PREMODERATION', 'Премодерация постов', 'YES'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'YES');