
INSERT INTO tags(id, name) VALUES
(1, 'игры'),
(2, 'телефон'),
(3, 'алиэкспресс'),
(4, 'технологии'),
(5, 'новинки');

INSERT INTO captcha_codes(id, time, code, secret_code) VALUES
(1, '2006-12-23 23:20:32', '85K21', '85K21'),
(2, '2006-11-23 05:4:15', '85K22', '85K22'),
(3, '2000-01-01 00:00:08', '85K23', '85K23'),
(4, '2000-01-01 00:00:08', '85K24', '85K24'),
(5, '2000-01-01 00:00:08', '85K25', '85K25');

INSERT INTO users(id, email, is_moderator, reg_time, name, password) VALUES
(1,  'tarakan@mail.ru', 1, '2014-09-30', 'Bonita', 'qwerty'),
(2, 'WaltraudK.Pearson96@example.com', 0, '2014-10-10',  'Ernie', 'qwerty1'),
(3, 'dydqi814@example.com', 0, '2014-11-10',  'Hosea', 'qwerty1'),
(4, 'ElliottSkaggs@example.com', 0, '2014-12-10', 'Maryanna', 'qwerty12' ),
(5, 'HerschelKendall72@example.com', 0, '2015-01-10', 'Hulk', 'qwerty123');

INSERT INTO global_settings(id, code, name, value) VALUES
(1, 'MULTIUSER_MODE', 'Многопользовательский режим', 'NO'),
(2, 'POST_PREMODERATION', 'Премодерация постов', 'YES'),
(3, 'STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'YES');

INSERT INTO posts(id, time, user_id, title, text,  is_active, moderation_status, moderator_id, view_count) VALUES
(1, '2016-01-01 00:28:11', 1, 'Non nostrum dignissimos.', 'Focusing on the latest investigations, we can positively say that the example of the feedback system must stay true to an importance of the well-known practice.  ', 1, 'ACCEPTED', 1, 5),
(2, '2016-11-10 03:2:13', 2, 'Doloremque qui rerum.', 'The most common argument against this is that the initial progress in the specific decisions reinforces the argument for any contemporary or prominent approach.  ', 1, 'ACCEPTED', 1, 5),
(3, '2016-12-09 07:15:10', 3, 'Nulla fuga exercitationem.', 'To straighten it out, the conventional notion of the arrangement of the system mechanism provides rich insights into the predictable behavior.  ', 1, 'ACCEPTED', 1, 5),
(4, '2016-07-13 07:19:30', 4, 'Ad voluptate laudantium.', 'One cannot deny that a number of brand new approaches has been tested during the the improvement of the final draft.',  1, 'ACCEPTED', 1, 5),
(5, '2016-01-01 00:15:34', 5, 'Numquam et eveniet.', 'In respect that any technical requirements impacts fully on every grand strategy. In respect of the condition of the flexible production planning may share attitudes on the potential role models.',  0, 'NEW', null, 0);

INSERT INTO post_comments(id, parent_comment_id, post_id, user_id, time, text) VALUES
(1, null, 1, 1, '2015-04-05 03:35:01', 'Test comment'),
(2, 1, 1, 1, '2015-05-05 03:35:01', 'Second'),
(3, 2, 1, 2, '2015-04-05 03:35:01', 'First'),
(4, null, 2, 2, '2015-04-05 03:35:01', 'Your advertising can be here'),
(5, null, 3, 2, '2015-04-05 03:35:01', 'It is plan B ');

INSERT INTO post_votes(id, user_id, post_id, time, value) VALUES
(1, 1, 1, '2017-01-01 00:28:11', 1),
(2, 2, 1, '2017-02-01 00:28:11', 1),
(3, 3, 1, '2017-03-01 00:28:11', -1);

INSERT INTO tag2post(post_id, tag_id) VALUES
(1, 1),
(1, 2),
(2, 2);