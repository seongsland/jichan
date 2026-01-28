-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: jichan
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contact_log`
--

DROP TABLE IF EXISTS `contact_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contact_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `viewer_id` bigint NOT NULL,
  `expert_id` bigint NOT NULL,
  `contact_type` enum('EMAIL','PHONE') COLLATE utf8mb4_general_ci NOT NULL,
  `viewed_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_CONTACT_LOG` (`viewer_id`,`expert_id`,`contact_type`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_log`
--

LOCK TABLES `contact_log` WRITE;
/*!40000 ALTER TABLE `contact_log` DISABLE KEYS */;
INSERT INTO `contact_log` VALUES (24,24,24,'PHONE','2026-01-23 14:42:07.244928'),(25,24,24,'EMAIL','2026-01-23 14:42:10.023902'),(26,25,24,'EMAIL','2026-01-26 11:52:50.217543'),(30,18,16,'EMAIL','2026-01-27 10:04:20.326744'),(31,18,14,'PHONE','2026-01-27 10:04:28.330135'),(32,18,13,'PHONE','2026-01-27 10:04:35.482229'),(33,18,12,'PHONE','2026-01-27 10:04:46.218388'),(34,19,17,'EMAIL','2026-01-27 10:09:35.637802'),(35,19,17,'PHONE','2026-01-27 10:09:37.284397'),(36,19,18,'EMAIL','2026-01-27 10:09:42.560360'),(37,19,16,'EMAIL','2026-01-27 10:09:48.986142'),(38,19,19,'EMAIL','2026-01-27 10:10:24.634882'),(39,20,20,'EMAIL','2026-01-27 10:13:50.609302'),(40,20,19,'EMAIL','2026-01-27 10:13:54.157962'),(41,20,18,'EMAIL','2026-01-27 10:13:56.271371'),(42,20,17,'EMAIL','2026-01-27 10:13:58.877504'),(43,20,16,'EMAIL','2026-01-27 10:14:00.351988'),(44,25,13,'PHONE','2026-01-27 10:15:05.167213'),(45,25,15,'PHONE','2026-01-27 10:15:08.077742'),(46,25,14,'PHONE','2026-01-27 10:15:11.338204'),(47,25,12,'PHONE','2026-01-27 10:15:12.966671'),(48,25,25,'EMAIL','2026-01-27 10:15:32.941119'),(49,24,18,'EMAIL','2026-01-27 10:17:05.449219'),(50,24,17,'EMAIL','2026-01-27 10:17:11.937565'),(51,24,13,'PHONE','2026-01-27 10:17:15.767016'),(52,24,12,'PHONE','2026-01-27 10:17:17.134597');
/*!40000 ALTER TABLE `contact_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `expert_id` bigint NOT NULL,
  `score` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_RATING` (`user_id`,`expert_id`),
  KEY `idx_rating_1` (`expert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES (16,18,14,4,'2026-01-27 10:04:56.975096','2026-01-27 10:04:56.975096'),(17,18,13,5,'2026-01-27 10:05:00.218717','2026-01-27 10:05:00.218717'),(18,18,16,5,'2026-01-27 10:05:03.675288','2026-01-27 10:05:03.675288'),(19,18,12,3,'2026-01-27 10:05:07.409778','2026-01-27 10:05:07.409778'),(20,19,17,5,'2026-01-27 10:09:52.317702','2026-01-27 10:09:52.317702'),(21,19,16,5,'2026-01-27 10:09:54.031005','2026-01-27 10:09:54.031005'),(22,19,18,5,'2026-01-27 10:09:55.122985','2026-01-27 10:09:55.122985'),(23,19,19,5,'2026-01-27 10:10:29.200498','2026-01-27 10:10:29.200498'),(24,20,16,5,'2026-01-27 10:14:03.222826','2026-01-27 10:14:03.222826'),(25,20,17,4,'2026-01-27 10:14:06.273460','2026-01-27 10:14:06.273460'),(26,20,18,5,'2026-01-27 10:14:09.492013','2026-01-27 10:14:09.492013'),(27,20,19,5,'2026-01-27 10:14:13.166950','2026-01-27 10:14:13.166950'),(28,20,20,5,'2026-01-27 10:14:14.118562','2026-01-27 10:14:14.118562'),(29,25,12,5,'2026-01-27 10:15:18.193993','2026-01-27 10:15:18.193993'),(30,25,13,4,'2026-01-27 10:15:21.256190','2026-01-27 10:15:21.256190'),(31,25,14,4,'2026-01-27 10:15:24.590638','2026-01-27 10:15:24.590638'),(32,25,15,4,'2026-01-27 10:15:26.545997','2026-01-27 10:15:26.545997'),(33,25,25,5,'2026-01-27 10:15:35.942412','2026-01-27 10:15:35.942412'),(34,24,12,5,'2026-01-27 10:17:19.642790','2026-01-27 10:17:19.642790'),(35,24,13,5,'2026-01-27 10:17:22.549960','2026-01-27 10:17:22.549960'),(36,24,17,5,'2026-01-27 10:17:25.772597','2026-01-27 10:17:25.772597'),(37,24,18,1,'2026-01-27 10:17:31.139013','2026-01-27 10:17:31.139013'),(38,24,24,5,'2026-01-27 10:17:32.429718','2026-01-27 10:17:32.429718');
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialty_category`
--

DROP TABLE IF EXISTS `specialty_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialty_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty_category`
--

LOCK TABLES `specialty_category` WRITE;
/*!40000 ALTER TABLE `specialty_category` DISABLE KEYS */;
INSERT INTO `specialty_category` VALUES (4,'서버',1),(5,'프로그래밍',2),(6,'디자인',3),(7,'멀티미디어',4),(8,'마케팅',5),(9,'창업',6),(10,'법률',7),(11,'번역',8),(12,'문서',9),(13,'집',10),(14,'자동차',11),(15,'취미',12),(16,'상담',13),(17,'운세',14),(18,'기타',15);
/*!40000 ALTER TABLE `specialty_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialty_detail`
--

DROP TABLE IF EXISTS `specialty_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialty_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `IDX_SPECIALTY_DETAIL_ORDER` (`category_id`,`sort_order`),
  CONSTRAINT `FK_SPECIALTY_DETAIL_SPEICAL_CATEGORY` FOREIGN KEY (`category_id`) REFERENCES `specialty_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty_detail`
--

LOCK TABLES `specialty_detail` WRITE;
/*!40000 ALTER TABLE `specialty_detail` DISABLE KEYS */;
INSERT INTO `specialty_detail` VALUES (6,4,'윈도우 관리',1),(7,4,'유닉스 계열 관리',2),(8,4,'DB 서버 관리',3),(9,4,'AWS 관리',4),(10,4,'기타',5),(11,5,'Vue',1),(12,5,'React',2),(13,5,'PHP',3),(14,5,'JAVA',4),(15,5,'.NET',5),(16,5,'Android',6),(17,5,'IOS',7),(18,5,'기타',8),(19,6,'웹',1),(20,6,'로고',2),(21,6,'배너',3),(22,6,'인쇄물',4),(23,6,'기타',5),(24,7,'동영상 제작',1),(25,7,'사운드 제작',2),(26,7,'기타',3),(27,8,'온라인 광고',1),(28,8,'오프라인 광고',2),(29,8,'SEO 최적화',3),(30,8,'기타',4),(31,9,'온라인 창업 자문',1),(32,9,'오프라인 창업 자문',2),(33,9,'사업계획서 자문',3),(34,9,'기타',4),(35,10,'법률 자문',1),(36,10,'회계 관련',2),(37,10,'특허 관련',3),(38,10,'노무 상담',4),(39,10,'기타',5),(40,11,'영어 번역',1),(41,11,'중국어 번역',2),(42,11,'일본어 번역',3),(43,11,'기타',4),(44,12,'단순 타이핑',1),(45,12,'교정 첨삭',2),(46,12,'원고 작성',3),(47,12,'기타',4),(48,13,'방수 공사',1),(49,13,'화장실 수리',2),(50,13,'싱크대',3),(51,13,'도배',4),(52,13,'기타',5),(53,14,'세차',1),(54,14,'튜닝',2),(55,14,'수리',3),(56,14,'중고차 판매',4),(57,14,'중고차 구매',5),(58,14,'기타',6),(59,15,'운동',1),(60,15,'뷰티',2),(61,15,'패션',3),(62,15,'악기',4),(63,15,'보컬',5),(64,15,'댄스',6),(65,15,'골프',7),(66,15,'미술',8),(67,15,'공예',9),(68,15,'가죽',10),(69,15,'미싱',11),(70,15,'기타',12),(71,16,'심리',1),(72,16,'연애',2),(73,16,'고민',3),(74,16,'기타',4),(75,17,'작명',1),(76,17,'궁합',2),(77,17,'사주',3),(78,17,'손금',4),(79,17,'기타',5),(80,18,'기타',1);
/*!40000 ALTER TABLE `specialty_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `is_visible` bit(1) NOT NULL,
  `gender` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `region` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `introduction` text COLLATE utf8mb4_general_ci,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone_message` text COLLATE utf8mb4_general_ci,
  `email_verified` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `average_rating` int NOT NULL DEFAULT '0',
  `review_count` int NOT NULL DEFAULT '0',
  `min_hourly_rate` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USER` (`email`),
  KEY `idx_user_1` (`is_visible`,`region`,`average_rating` DESC),
  KEY `idx_user_2` (`is_visible`,`region`,`min_hourly_rate`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (12,'seongsland1@gmail.com','마인드힐러','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','여성','경기도 부천시','요즘 힘드신가요?\n마음을 편하게 힐링해 보세요.\n심리 및 고민 상담 전문가 입니다.\n다른 상담도 환영하지만 퀄리티가 조금 떨어질 수 있어요~','010-0000-0001','평일, 주말 상관없이 오전 9시부터 오후 7시 전까지 상담 가능합니다.',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:17:19.645953',4,3,30000),(13,'seongsland2@gmail.com','미싱드드득','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','여성','부산광역시 해운대구','미싱으로 만드는걸 좋아하는 사람이에요.\n미싱의 기본적인 사용법 같은거 물어보시면 알려드릴게요.\n만들고 싶으신 것이 있으면 미싱 패턴도 공유해 드려요.\n수선이 필요하거나 하시면 저희 공방에도 놀러오세요.\n연락 주시면 안내해 드릴게요.','010-0000-0002','평일 오전 9시~오후5시까지 연락 가능합니다.',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:17:22.552959',5,3,20000),(14,'seongsland3@gmail.com','매의눈','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','인천광역시 부평구','중고차 구매할 계획이신가요?\n중고차 구매시 같이 동행해서 상태 봐 드립니다.\n인천 전지역 가능하시고요.\n같이 동행해서 중고차 상태 및 가격이 적당한지 알려드릴게요.\n중고차 판매 알바만 10년 하다가 지금은 다른 것 하고 있어요.','010-0000-0003','토,일 아침 9시~오후 10시까지 가능합니다.\n중고차 구매하시려면 미리 연락을 주시고 약속을 잡으시면 됩니다.',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:15:24.593639',4,2,200000),(15,'seongsland4@gmail.com','방수맨','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','서울특별시 마포구','외벽, 옥상 방수처리 제대로 해드립니다.\n재료비 + 인건비 주시면 되고, 재료비는 영수증 첨부해 드립니다.\n투명하게 작업 진행해 드립니다.\n연락주시면 방문 후 상태 확인하고 총 견적 안내 드립니다.\n마포구에서 1시간 이내 거리만 가능합니다.','010-0000-0005','TEST004',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:15:26.549999',4,1,100000),(16,'seongsland5@gmail.com','바이럴마스터','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','전라남도','온라인 광고 상담해 드립니다.\n온라인 광고 가장 싸게 가장 효과적으로 하는 방법 안내해 드리고,\n끝까지 책임지고 효과적인 결과물이 나올때까지 계속 피드백 드리겠습니다.','','',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:14:03.225334',5,3,50000),(17,'seongsland6@gmail.com','드자이너팍','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','여성','제주특별자치도','현재 제주도에서 디지털 노마드 중이에요!\n웹에 들어가는 각종 디자인 작업해 드립니다.\n메인 페이지, 서브 페이지, 배너, 등등 다 가능해요.\n편하게 연락주세요~','010-0000-0009','평일 09 ~ 20시까지 연락 가능합니다.',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:17:25.775611',5,3,30000),(18,'seongsland7@gmail.com','성쓰','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','인천광역시 계양구','20년 경력의 백엔드 개발자입니다.\nJAVA, PHP, ASP, JSP, javascript, node.js, react, vue, jQuery 다 연락주세요.\n모르는 부분도 AI를 이용해서 다 해결해 드리도록 하겠습니다!\n만들고 싶으신 것을 말씀하시면 대략적인 기간과 금액 안내해 드릴게요.\n관련 자료를 같이 메일로 보내주세요.','','',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:17:31.142014',4,3,30000),(19,'seongsland8@gmail.com','AWSER','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','서울특별시 노원구','AWS 관련 세팅 및 관리 해드립니다.\n서울시내 출장 가능하며, 정기적인 운영 계약도 가능합니다. (하루에 몇 시간만 계약 가능)\n출장시 소요시간 포함해서 금액을 책정합니다.','010-0000-0010','오전8시~오후8시까지 편하게 연락주세요.',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:14:13.170056',5,2,50000),(20,'seongsland9@gmail.com','오함마','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','서울특별시 강북구','MS-SQL 서버 설정 및 관리 가능합니다.\n정책 변경, 백업, 복구, 마이그레이션 모두 가능합니다.\n연락주세요.','010-0000-0020','평일 오후 7시~오후 10시까지 가능합니다.\n주말은 오전 9시~오후 10시까지 가능합니다.',_binary '','2026-01-15 10:52:28.722179','2026-01-27 10:14:14.121602',5,1,70000),(24,'seongsland@gmail.com','seongsland','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','서울특별시 용산구','안녕하세요.\n저는 Backend 개발자 입니다.\n제가 할 수 있는 일이라면 연락주세요.','010-2860-0000','평일 오전 10시부터 오후 6시까지만 통화 가능합니다.',_binary '','2026-01-23 14:29:11.698930','2026-01-27 10:19:42.402016',5,1,40000),(25,'seongsgc@gmail.com','불패','$2a$10$.lI31uApFZxeZwi81HlhROAigb/AWMJPTcpGOIitcLhkOgqg5wSme',_binary '','남성','서울특별시 강서구','저는 윈도우 서버 관리 전문가 입니다.\n저에게 연락 주시면 친절하게 안내 해 드리겠습니다.\n적당한 가격으로 최고의 서비스를 제공해 드립니다.','010-1234-1234','평일, 주말 상관없이 아무때나 연락 주세요.',_binary '','2026-01-26 11:33:59.252997','2026-01-27 10:15:35.944916',5,1,50000);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_specialty`
--

DROP TABLE IF EXISTS `user_specialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_specialty` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `specialty_detail_id` bigint NOT NULL,
  `hourly_rate` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_specialty_1` (`user_id`,`specialty_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_specialty`
--

LOCK TABLES `user_specialty` WRITE;
/*!40000 ALTER TABLE `user_specialty` DISABLE KEYS */;
INSERT INTO `user_specialty` VALUES (109,12,71,50000),(110,12,73,50000),(111,12,74,30000),(112,13,69,20000),(113,14,57,200000),(114,15,48,100000),(136,16,27,50000),(137,16,29,50000),(138,16,30,50000),(139,17,19,30000),(140,17,21,30000),(141,17,23,30000),(143,18,14,30000),(144,18,13,30000),(145,18,18,30000),(146,19,9,50000),(147,20,8,70000),(148,25,6,50000),(151,24,14,40000),(152,24,18,40000);
/*!40000 ALTER TABLE `user_specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'jichan'
--

--
-- Dumping routines for database 'jichan'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-28 13:57:35
