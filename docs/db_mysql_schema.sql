create table jichan.specialty_category
(
id   bigint auto_increment       primary key,
name varchar(255) not null,
sort_order int not null default 0
);

create table jichan.specialty_detail
(
id          bigint auto_increment       primary key,
category_id bigint       not null,
name        varchar(255) not null,
sort_order int not null default 0,
constraint FK_SPECIALTY_DETAIL_SPECIAL_CATEGORY foreign key (category_id) references jichan.specialty_category (id)
);
CREATE INDEX idx_specialty_detail_1 ON jichan.specialty_detail (category_id, sort_order);

create table jichan.user
(
id             bigint auto_increment    primary key,
email          varchar(255) not null,
name           varchar(255) not null,
password       varchar(255) not null,
is_visible     bit          not null,
gender         varchar(255) null,
region         varchar(255) null,
introduction   text         null,
phone          varchar(255) null,
phone_message  text         null,
email_verified bit          not null,
created_at     datetime(6)  null,
updated_at     datetime(6)  null,
average_rating int NOT NULL DEFAULT '0',
review_count    int NOT NULL DEFAULT '0',
min_hourly_rate int NOT NULL DEFAULT '0',
constraint UK_USER    unique (email)
);
CREATE INDEX idx_user_1 ON user (is_visible, region, average_rating DESC);
CREATE INDEX idx_user_2 ON user (is_visible, region, min_hourly_rate ASC);


create table jichan.user_specialty
(
id                  bigint auto_increment        primary key,
user_id             bigint not null,
specialty_detail_id bigint not null,
hourly_rate         int    not null
);
create index idx_user_specialty_1 on user_specialty (user_id, specialty_detail_id);


create table jichan.contact_log
(
id           bigint auto_increment        primary key,
viewer_id    bigint                  not null,
expert_id    bigint                  not null,
contact_type enum ('EMAIL', 'PHONE') not null,
viewed_at    datetime(6)             not null,
constraint UK_CONTACT_LOG  unique (viewer_id, expert_id, contact_type)
);

create table jichan.rating
(
id         bigint auto_increment       primary key,
user_id    bigint      not null,
expert_id  bigint      not null,
score      int         not null,
created_at datetime(6) null,
updated_at datetime(6) null,
constraint UK_RATING        unique (user_id, expert_id)
);
create index idx_rating_1 on rating (expert_id);
