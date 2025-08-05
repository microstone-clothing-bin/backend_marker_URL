# 1단계: 빌드
FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN ./gradlew bootJar --no-daemon

# 2단계: 실행 이미지
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]