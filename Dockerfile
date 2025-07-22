# STEP 1: Gradle을 사용해서 빌드하는 단계
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

# STEP 2: 빌드 결과 JAR만 복사해서 실행
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/cloth_area-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
