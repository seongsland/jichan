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


-- 1. 서버
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('서버', 1);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '윈도우 관리', 1), (@last_id, '유닉스 계열 관리', 2), (@last_id, 'DB 서버 관리', 3), (@last_id, 'AWS 관리', 4), (@last_id, '기타', 5);

-- 2. 프로그래밍
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('프로그래밍', 2);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, 'Vue', 1), (@last_id, 'React', 2), (@last_id, 'PHP', 3), (@last_id, 'JAVA', 4), (@last_id, '.NET', 5), (@last_id, 'Android', 6), (@last_id, 'IOS', 7), (@last_id, '기타', 8);

-- 3. 디자인
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('디자인', 3);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '웹', 1), (@last_id, '로고', 2), (@last_id, '배너', 3), (@last_id, '인쇄물', 4), (@last_id, '기타', 5);

-- 4. 멀티미디어
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('멀티미디어', 4);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '동영상 제작', 1), (@last_id, '사운드 제작', 2), (@last_id, '기타', 3);

-- 5. 마케팅
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('마케팅', 5);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '온라인 광고', 1), (@last_id, '오프라인 광고', 2), (@last_id, 'SEO 최적화', 3), (@last_id, '기타', 4);

-- 6. 창업
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('창업', 6);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '온라인 창업 자문', 1), (@last_id, '오프라인 창업 자문', 2), (@last_id, '사업계획서 자문', 3), (@last_id, '기타', 4);

-- 7. 법률
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('법률', 7);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '법률 자문', 1), (@last_id, '회계 관련', 2), (@last_id, '특허 관련', 3), (@last_id, '노무 상담', 4), (@last_id, '기타', 5);

-- 8. 번역
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('번역', 8);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '영어 번역', 1), (@last_id, '중국어 번역', 2), (@last_id, '일본어 번역', 3), (@last_id, '기타', 4);

-- 9. 문서
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('문서', 9);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '단순 타이핑', 1), (@last_id, '교정 첨삭', 2), (@last_id, '원고 작성', 3), (@last_id, '기타', 4);

-- 10. 집
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('집', 10);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '방수 공사', 1), (@last_id, '화장실 수리', 2), (@last_id, '싱크대', 3), (@last_id, '도배', 4), (@last_id, '기타', 5);

-- 11. 자동차
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('자동차', 11);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '세차', 1), (@last_id, '튜닝', 2), (@last_id, '수리', 3), (@last_id, '중고차 판매', 4), (@last_id, '중고차 구매', 5), (@last_id, '기타', 6);

-- 12. 취미
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('취미', 12);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '운동', 1), (@last_id, '뷰티', 2), (@last_id, '패션', 3), (@last_id, '악기', 4), (@last_id, '보컬', 5), (@last_id, '댄스', 6), (@last_id, '골프', 7), (@last_id, '미술', 8), (@last_id, '공예', 9), (@last_id, '가죽', 10), (@last_id, '미싱', 11), (@last_id, '기타', 12);

-- 13. 상담
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('상담', 13);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '심리', 1), (@last_id, '연애', 2), (@last_id, '고민', 3), (@last_id, '기타', 4);

-- 14. 운세
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('운세', 14);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
                                                                        (@last_id, '작명', 1), (@last_id, '궁합', 2), (@last_id, '사주', 3), (@last_id, '손금', 4), (@last_id, '기타', 5);

-- 15. 기타
INSERT INTO jichan.specialty_category (name, sort_order) VALUES ('기타', 15);
SET @last_id = LAST_INSERT_ID();
INSERT INTO jichan.specialty_detail (category_id, name, sort_order) VALUES
    (@last_id, '기타', 1);

