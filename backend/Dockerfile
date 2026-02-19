# 1. 실행 환경만 준비
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 2. 이미 내 컴퓨터에 빌드된 jar 파일만 복사
COPY build/libs/*.jar app.jar

# 3. 실행
ENTRYPOINT ["java", "-Xmx384m", "-Xms384m", "-jar", "app.jar"]