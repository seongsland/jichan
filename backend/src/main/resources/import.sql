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