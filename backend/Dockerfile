# 1. 빌드 스테이지: JDK를 사용하여 jar 파일을 빌드합니다.
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
# 실행 권한 부여 및 빌드 (테스트는 시간 단축을 위해 제외)
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# 2. 실행 스테이지: 빌드된 jar만 가져와서 실행합니다. (이미지 최적화)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# 환경 변수 설정 (나중에 DB 연결 시 활용)
ENTRYPOINT ["java", "-jar", "app.jar"]