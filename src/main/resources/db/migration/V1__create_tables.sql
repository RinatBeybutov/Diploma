


create table captcha_codes (
    id integer not null AUTO_INCREMENT,
    code varchar(255) not null,
    secret_code varchar(255) not null,
    time datetime not null,
    primary key (id));

create table global_settings (
    id integer not null auto_increment,
    code varchar(255) not null,
    name varchar(255) not null,
    value varchar(255) not null,
    primary key (id));

create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );

create table post_comments (
    id integer not null auto_increment,
    text varchar(255) not null,
    time datetime not null,
    parent_comment_id integer,
    post_id integer,
    user_id integer,
    primary key (id));

create table post_votes (
    id integer not null auto_increment,
    value tinyint not null,
    time datetime not null,
    post_id integer,
    user_id integer,
    primary key (id));

create table posts (
    id integer not null auto_increment,
    view_count integer,
    is_active tinyint not null,
    moderation_status varchar(255) not null,
    text varchar(255) not null,
    time datetime not null,
    title varchar(255) not null,

    moderator_id integer,
    user_id integer,
    primary key (id));

create table tag2post (
    post_id integer not null,
    tag_id integer not null);

create table tags (
    id integer not null auto_increment,
    name varchar(255) not null,
    primary key (id));

create table users (
    id integer not null auto_increment,
    code varchar(255),
    email varchar(255) not null,
    is_moderator tinyint not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo varchar(255),
    reg_time datetime not null,
    primary key (id));

alter table post_comments
    add constraint FK21q7y8a124im4g0l4aaxn4ol1 foreign key (parent_comment_id)
    references post_comments (id);

alter table post_comments
    add constraint FKaawaqxjs3br8dw5v90w7uu514 foreign key (post_id)
    references posts (id);

alter table post_comments
    add constraint FKsnxoecngu89u3fh4wdrgf0f2g foreign key (user_id)
    references users (id);

alter table post_votes
    add constraint FK9jh5u17tmu1g7xnlxa77ilo3u foreign key (post_id)
    references posts (id);

alter table post_votes
    add constraint FK9q09ho9p8fmo6rcysnci8rocc foreign key (user_id)
    references users (id);

alter table posts
    add constraint FK6m7nr3iwh1auer2hk7rd05riw foreign key (moderator_id)
    references users (id);

alter table posts
    add constraint FK5lidm6cqbc7u4xhqpxm898qme foreign key (user_id)
    references users (id);

alter table tag2post
    add constraint FKjou6suf2w810t2u3l96uasw3r foreign key (tag_id)
    references tags (id);

alter table tag2post
    add constraint FKpjoedhh4h917xf25el3odq20i foreign key (post_id)
    references posts (id);