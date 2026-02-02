-- 1. 서버
INSERT INTO specialty_category (name, sort_order) VALUES ('서버', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '서버'), '윈도우 관리', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '서버'), '유닉스 계열 관리', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '서버'), 'DB 서버 관리', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '서버'), 'AWS 관리', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '서버'), '기타', 999);

-- 2. 프로그래밍
INSERT INTO specialty_category (name, sort_order) VALUES ('프로그래밍', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), 'Vue', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), 'React', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), 'PHP', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), 'JAVA', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), '.NET', 5);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), 'Android', 6);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), 'IOS', 7);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '프로그래밍'), '기타', 999);

-- 3. 디자인
INSERT INTO specialty_category (name, sort_order) VALUES ('디자인', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '디자인'), '웹', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '디자인'), '로고', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '디자인'), '배너', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '디자인'), '인쇄물', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '디자인'), '기타', 999);

-- 4. 멀티미디어
INSERT INTO specialty_category (name, sort_order) VALUES ('멀티미디어', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '멀티미디어'), '동영상 제작', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '멀티미디어'), '사운드 제작', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '멀티미디어'), '기타', 999);

-- 5. 마케팅
INSERT INTO specialty_category (name, sort_order) VALUES ('마케팅', 5);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '마케팅'), '온라인 광고', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '마케팅'), '오프라인 광고', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '마케팅'), 'SEO 최적화', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '마케팅'), '기타', 999);

-- 6. 창업
INSERT INTO specialty_category (name, sort_order) VALUES ('창업', 6);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '창업'), '온라인 창업 자문', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '창업'), '오프라인 창업 자문', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '창업'), '사업계획서 자문', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '창업'), '기타', 999);

-- 7. 법률
INSERT INTO specialty_category (name, sort_order) VALUES ('법률', 7);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '법률'), '법률 자문', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '법률'), '회계 관련', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '법률'), '특허 관련', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '법률'), '노무 상담', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '법률'), '기타', 999);

-- 8. 번역
INSERT INTO specialty_category (name, sort_order) VALUES ('번역', 8);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '번역'), '영어 번역', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '번역'), '중국어 번역', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '번역'), '일본어 번역', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '번역'), '기타', 999);

-- 9. 문서
INSERT INTO specialty_category (name, sort_order) VALUES ('문서', 9);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '문서'), '단순 타이핑', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '문서'), '교정 첨삭', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '문서'), '원고 작성', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '문서'), '기타', 999);

-- 10. 집
INSERT INTO specialty_category (name, sort_order) VALUES ('집', 10);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '집'), '방수 공사', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '집'), '화장실 수리', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '집'), '싱크대', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '집'), '도배', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '집'), '기타', 999);

-- 11. 자동차
INSERT INTO specialty_category (name, sort_order) VALUES ('자동차', 11);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '자동차'), '세차', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '자동차'), '튜닝', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '자동차'), '수리', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '자동차'), '중고차 판매', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '자동차'), '중고차 구매', 5);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '자동차'), '기타', 999);

-- 12. 취미
INSERT INTO specialty_category (name, sort_order) VALUES ('취미', 12);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '운동', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '뷰티', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '패션', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '악기', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '보컬', 5);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '댄스', 6);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '골프', 7);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '미술', 8);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '공예', 9);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '가죽', 10);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '미싱', 11);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '게임', 12);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '취미'), '기타', 999);

-- 13. 상담
INSERT INTO specialty_category (name, sort_order) VALUES ('상담', 13);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '상담'), '심리', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '상담'), '연애', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '상담'), '고민', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '상담'), '기타', 999);

-- 14. 운세
INSERT INTO specialty_category (name, sort_order) VALUES ('운세', 14);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '운세'), '작명', 1);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '운세'), '궁합', 2);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '운세'), '사주', 3);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '운세'), '손금', 4);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '운세'), '기타', 999);

-- 15. 기타
INSERT INTO specialty_category (name, sort_order) VALUES ('기타', 999);
INSERT INTO specialty_detail (category_id, name, sort_order) VALUES ((SELECT id FROM specialty_category WHERE name = '기타'), '기타', 999);

-- user
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test1@gmail.com', '마인드힐러', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '여성', '경기도 부천시', '요즘 힘드신가요?'||CHAR(10)||'마음을 편하게 힐링해 보세요.'||CHAR(10)||'심리 및 고민 상담 전문가 입니다.'||CHAR(10)||'다른 상담도 환영하지만 퀄리티가 조금 떨어질 수 있어요~', '010-0000-0001', '평일, 주말 상관없이 오전 9시부터 오후 7시 전까지 상담 가능합니다.', true, '2026-01-15 10:52:28.722179', '2026-01-30 16:09:02.001172', 4, 2, 30000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test2@gmail.com', '미싱드드득', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '여성', '부산광역시 해운대구', '미싱으로 만드는걸 좋아하는 사람이에요.'||CHAR(10)||'미싱의 기본적인 사용법 같은거 물어보시면 알려드릴게요.'||CHAR(10)||'만들고 싶으신 것이 있으면 미싱 패턴도 공유해 드려요.'||CHAR(10)||'수선이 필요하거나 하시면 저희 공방에도 놀러오세요.'||CHAR(10)||'연락 주시면 안내해 드릴게요.', '010-0000-0002', '평일 오전 9시~오후5시까지 연락 가능합니다.', true, '2026-01-15 10:52:28.722179', '2026-01-30 13:44:10.284549', 5, 3, 20000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test3@gmail.com', '매의눈', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '인천광역시 부평구', '중고차 구매할 계획이신가요?'||CHAR(10)||'중고차 구매시 같이 동행해서 상태 봐 드립니다.'||CHAR(10)||'인천 전지역 가능하시고요.'||CHAR(10)||'같이 동행해서 중고차 상태 및 가격이 적당한지 알려드릴게요.'||CHAR(10)||'중고차 판매 알바만 10년 하다가 지금은 다른 것 하고 있어요.', '010-0000-0003', '토,일 아침 9시~오후 10시까지 가능합니다.'||CHAR(10)||'중고차 구매하시려면 미리 연락을 주시고 약속을 잡으시면 됩니다.', true, '2026-01-15 10:52:28.722179', '2026-01-27 10:15:24.593639', 4, 2, 200000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test4@gmail.com', '방수맨', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '서울특별시 마포구', '외벽, 옥상 방수처리 제대로 해드립니다.'||CHAR(10)||'재료비 + 인건비 주시면 되고, 재료비는 영수증 첨부해 드립니다.'||CHAR(10)||'투명하게 작업 진행해 드립니다.'||CHAR(10)||'연락주시면 방문 후 상태 확인하고 총 견적 안내 드립니다.'||CHAR(10)||'마포구에서 1시간 이내 거리만 가능합니다.', '010-0000-0004', 'TEST004', true, '2026-01-15 10:52:28.722179', '2026-01-27 10:15:26.549999', 4, 1, 100000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test5@gmail.com', '바이럴마스터', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '전라남도', '온라인 광고 상담해 드립니다.'||CHAR(10)||'온라인 광고 가장 싸게 가장 효과적으로 하는 방법 안내해 드리고,'||CHAR(10)||'끝까지 책임지고 효과적인 결과물이 나올때까지 계속 피드백 드리겠습니다.', '', '', true, '2026-01-15 10:52:28.722179', '2026-01-27 10:14:03.225334', 5, 3, 50000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test6@gmail.com', '드자이너팍', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '여성', '제주특별자치도', '현재 제주도에서 디지털 노마드 중이에요!'||CHAR(10)||'웹에 들어가는 각종 디자인 작업해 드립니다.'||CHAR(10)||'메인 페이지, 서브 페이지, 배너, 등등 다 가능해요.'||CHAR(10)||'편하게 연락주세요~', '010-0000-0005', '평일 09 ~ 20시까지 연락 가능합니다.', true, '2026-01-15 10:52:28.722179', '2026-01-27 10:17:25.775611', 5, 3, 30000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test7@gmail.com', '성쓰', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '인천광역시 계양구', '20년 경력의 백엔드 개발자입니다.'||CHAR(10)||'JAVA, PHP, ASP, JSP, javascript, node.js, react, vue, jQuery 다 연락주세요.'||CHAR(10)||'모르는 부분도 AI를 이용해서 다 해결해 드리도록 하겠습니다!'||CHAR(10)||'만들고 싶으신 것을 말씀하시면 대략적인 기간과 금액 안내해 드릴게요.'||CHAR(10)||'관련 자료를 같이 메일로 보내주세요.', '', '', true, '2026-01-15 10:52:28.722179', '2026-01-30 15:46:08.616420', 4, 3, 30000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test8@gmail.com', 'AWSER', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '서울특별시 노원구', 'AWS 관련 세팅 및 관리 해드립니다.'||CHAR(10)||'서울시내 출장 가능하며, 정기적인 운영 계약도 가능합니다. (하루에 몇 시간만 계약 가능)'||CHAR(10)||'출장시 소요시간 포함해서 금액을 책정합니다.', '010-0000-0006', '오전8시~오후8시까지 편하게 연락주세요.', true, '2026-01-15 10:52:28.722179', '2026-01-27 10:14:13.170056', 5, 2, 50000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test9@gmail.com', '오함마', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '서울특별시 강북구', 'MS-SQL 서버 설정 및 관리 가능합니다.'||CHAR(10)||'정책 변경, 백업, 복구, 마이그레이션 모두 가능합니다.'||CHAR(10)||'연락주세요.', '010-0000-0007', '평일 오후 7시~오후 10시까지 가능합니다.'||CHAR(10)||'주말은 오전 9시~오후 10시까지 가능합니다.', true, '2026-01-15 10:52:28.722179', '2026-01-27 10:14:14.121602', 5, 1, 70000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test@gmail.com', 'test', '$2a$10$JLldCyqA24GMll9cmMyr8.lA87K9CRfISB07EkZgeqqVp6mkWNjwW', true, '남성', '서울특별시 용산구', '안녕하세요.'||CHAR(10)||'저는 Backend 개발자 입니다.'||CHAR(10)||'제가 할 수 있는 일이라면 연락주세요.', '010-0000-0008', '평일 오전 10시부터 오후 6시까지만 통화 가능합니다.', true, '2026-01-23 14:29:11.698930', '2026-01-30 16:26:30.573383', 4, 1, 40000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('test0@gmail.com', '불패', '$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme', true, '남성', '서울특별시 강서구', '저는 윈도우 서버 관리 전문가 입니다.'||CHAR(10)||'저에게 연락 주시면 친절하게 안내 해 드리겠습니다.'||CHAR(10)||'적당한 가격으로 최고의 서비스를 제공해 드립니다.', '010-0000-0009', '평일, 주말 상관없이 아무때나 연락 주세요.', true, '2026-01-26 11:33:59.252997', '2026-01-31 01:02:05.953456', 4, 2, 50000);
INSERT INTO user (email, name, password, is_visible, gender, region, introduction, phone, phone_message, email_verified, created_at, updated_at, average_rating, review_count, min_hourly_rate) VALUES ('jichan.help@gmail.com', 'jichan', '$2a$10$8rfM0ZSm41Ji8n5GPPGoS.kdrkPwxkVAiQRM64rwrnDQiQ23wZTQu', false, null, null, null, null, null, true, '2026-01-30 11:20:37.644953', '2026-01-30 11:22:58.627966', 0, 0, 0);

-- user_specialty
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test1@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '심리'), 50000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test1@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '고민'), 50000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test2@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '미싱'), 20000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test3@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '중고차 구매'), 200000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test4@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '방수 공사'), 100000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test5@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '온라인 광고'), 50000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test5@gmail.com'), (SELECT id FROM specialty_detail WHERE name = 'SEO 최적화'), 50000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test6@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '웹'), 30000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test7@gmail.com'), (SELECT id FROM specialty_detail WHERE name = 'JAVA'), 30000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test7@gmail.com'), (SELECT id FROM specialty_detail WHERE name = 'PHP'), 30000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test8@gmail.com'), (SELECT id FROM specialty_detail WHERE name = 'AWS 관리'), 50000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test9@gmail.com'), (SELECT id FROM specialty_detail WHERE name = 'DB 서버 관리'), 70000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test@gmail.com'), (SELECT id FROM specialty_detail WHERE name = '윈도우 관리'), 50000);
INSERT INTO user_specialty (user_id, specialty_detail_id, hourly_rate) VALUES ((SELECT id FROM user WHERE email = 'test0@gmail.com'), (SELECT id FROM specialty_detail WHERE name = 'JAVA'), 40000);