# 지찬 (지인찬스)

전문가 프로필을 기반으로 사용자와 전문가를 연결하는 플랫폼입니다.

## 프로젝트 구조

```
jichan/
├── backend/          # Spring Boot 백엔드
├── frontend/         # React 프론트엔드
└── docs/             # 프로젝트 산출물 및 문서
```

## 기술 스택

### Backend

- **Framework**: Spring Boot 3.2.12
- **Language**: Java 17 (Toolchain 21)
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Query Language**: QueryDSL 5.6.1
- **Authentication**: Spring Security, JWT (JSON Web Token)
- **API Documentation**: Springdoc OpenAPI (Swagger UI) 2.5.0
- **Testing**: JUnit 5, Spring Boot Test, Spring Security Test
- **Code Coverage**: Jacoco 0.8.12
- **Dependencies**:
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-web`
    - `spring-boot-starter-validation`
    - `spring-boot-starter-security`
    - `spring-boot-starter-mail`
    - `spring-boot-starter-cache`
    - `jjwt-(api, impl, jackson)`
    - `mysql-connector-j`
    - `lombok`
    - `querydsl-jpa`
    - `springdoc-openapi-starter-webmvc-ui`

### Frontend

- **Framework**: React 18
- **Build Tool**: Vite

## 환경 변수 설정

프로젝트 실행을 위해 `backend` 디렉토리 내에 `.env` 파일(또는 시스템 환경 변수) 설정이 필요합니다. 다음 변수들을 설정해주세요.

```properties
# Database Configuration
MYSQL_ROOT_PW=root 비밀번호 (docker 세팅시 사용)
MYSQL_USERNAME=사용자명
MYSQL_PASSWORD=비밀번호
# Mail Configuration (gmail 발송 기능)
MAIL_USERNAME=gmail 계정
MAIL_PASSWORD=gmail 앱 비밀번호
# Security Configuration
JWT_SECRET=JWT시크릿키(충분히 긴 문자열)
ENC_KEY=암호화키
# Spring Profile
# local: 로컬 개발 환경
# prod: 라이브 환경
SPRING_PROFILES_ACTIVE=local
# Frontend Address
FRONTEND_ADDRESS=프론트엔드 주소
```

프론트엔드 실행을 위해 `frontend` 디렉토리 내에 `.env` 파일(또는 시스템 환경 변수) 설정이 필요합니다. 다음 변수들을 설정해주세요.

```properties
# Frontend Configuration
VITE_API_URL=백엔드 API URL
```

## 시작하기

### 백엔드 실행

```bash
cd backend
./gradlew bootRun
```

### 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

## 프로젝트 산출물

`/docs` 디렉토리에서 프로젝트 관련 주요 산출물을 확인할 수 있습니다.

- **erd.uml**: 데이터베이스 ERD (Entity Relationship Diagram)
- **db_mysql_schema.sql**: MySQL 데이터베이스 스키마 SQL 파일
- **api.html / api.json**: API 명세서 (OpenAPI/Swagger)
- **redocly.yaml**: Redocly 설정 파일

## API 문서 (Swagger UI)

백엔드 서버 실행 후, 다음 URL에서 API 문서를 확인할 수 있습니다.

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 테스트 및 코드 커버리지

- **테스트 실행**: `./gradlew test`
- **Jacoco 리포트 생성**: 테스트 실행 시 `backend/build/reports/jacoco/test/html/index.html` 경로에 리포트가 생성됩니다. 이 리포트를 통해 테스트 코드 커버리지를
  확인할 수 있습니다.
