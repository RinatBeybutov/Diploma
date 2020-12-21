
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
(1, '2016-01-01 00:28:11', 1, 'Non nostrum dignissimos.', 'Focusing on the latest investigations, we can positively say that the example of the feedback system must stay true to an importance of the well-known practice.  ', 1, 'ACCEPTED', 1, 8),
(2, '2017-11-10 03:2:13', 2, 'Doloremque qui rerum.', 'The most common argument against this is that the initial progress in the specific decisions reinforces the argument for any contemporary or prominent approach.  ', 1, 'ACCEPTED', 1, 10),
(3, '2015-12-09 07:15:10', 3, 'Nulla fuga exercitationem.', 'To straighten it out, the conventional notion of the arrangement of the system mechanism provides rich insights into the predictable behavior.  ', 1, 'ACCEPTED', 1, 15),
(4, '2018-07-13 07:19:30', 4, 'Ad voluptate laudantium.', 'One cannot deny that a number of brand new approaches has been tested during the the improvement of the final draft.',  1, 'ACCEPTED', 1, 3),
(5, '2014-01-01 00:15:34', 5, 'Numquam et eveniet.', 'In respect that any technical requirements impacts fully on every grand strategy. In respect of the condition of the flexible production planning may share attitudes on the potential role models.',  0, 'NEW', null, 1),
(6, '2016-01-02 00:15:34', 1, 'Simple java programs', 'Here are I am providing 10 simple java programs. They are good for coding practice and can be used in interviews. Please try to write the solution yourself before looking at the answer.', 1, 'ACCEPTED', 1, 20),
(7, '2014-02-01 00:15:34', 2, 'Qui inventore est.', 'Besides, components of an overview of the strategic planning poses problems and challenges for both the internal network and the basic reason of the closely developed techniques.',1, 'ACCEPTED', 2, 5),
(8, '2014-03-01 00:15:34', 2, 'Veniam voluptas est.', 'On the contrary, the accurate predictions of the sources and influences of the overall scores may share attitudes on the benefits of data integrity. Thus a complete understanding is missing.', 1, 'ACCEPTED', 2, 6),
(9, '2014-04-01 00:15:34', 3, 'Rerum eos quam.', 'As for some of the feedback system, it is clear that the initial progress in the base configuration gives rise to the entire picture.', 1, 'ACCEPTED', 2, 7),
(10, '2014-05-01 00:15:34', 3, 'Sed quidem cupiditate.', 'The the layout of the commitment to quality assurance gives less satisfactory results.',1, 'ACCEPTED', 2, 8),
(11, '2014-06-01 00:15:34', 4, 'Nihil quos aspernatur.', 'Nevertheless, one should accept that the big impact provides a glimpse at the strategic planning. Thus a complete understanding is missing.  ',1, 'ACCEPTED', 2, 9),
(12, '2014-07-01 00:15:34', 4, 'Text for beginners', 'English texts for beginners to practice reading and comprehension online and for free. Practicing your comprehension of written English will both improve your vocabulary and understanding of grammar and word order.',1, 'ACCEPTED', 2, 10);

INSERT INTO post_comments(id, parent_comment_id, post_id, user_id, time, text) VALUES
(1, null, 1, 1, '2015-04-05 03:35:01', 'Test comment'),
(2, 1, 1, 1, '2015-05-05 03:35:01', 'Second'),
(3, 2, 1, 2, '2015-04-05 03:35:01', 'First'),
(4, null, 2, 2, '2015-04-05 03:35:01', 'Your advertising can be here'),
(5, 4, 2, 2, '2015-04-05 03:35:01', 'It is plan B '),
(6, NULL, 3, 3, '2015-05-05 03:35:01', 'Buy my computer'),
(7, null, 4, 3, '2015-06-05 03:35:01', 'This post is good'),
(8, 7, 4, 4, '2015-07-05 03:35:01' , 'Comment for advertising');

INSERT INTO post_votes(id, user_id, post_id, time, value) VALUES
(1, 1, 1, '2017-01-01 00:28:11', 1),
(2, 2, 1, '2017-02-01 00:28:11', 1),
(3, 3, 1, '2017-03-01 00:28:11', -1),
(4, 2, 1, '2017-04-01 00:28:11', 1),
(5, 1, 2, '2017-05-01 00:28:11', 1),
(6, 3, 2, '2017-06-01 00:28:11', 1),
(7, 4, 3, '2017-07-01 00:28:11', 1),
(8, 5, 3, '2017-04-01 00:28:11', -1);


INSERT INTO tag2post(post_id, tag_id) VALUES
(1, 1),
(1, 2),
(2, 2);