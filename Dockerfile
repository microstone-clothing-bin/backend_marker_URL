# 1단계: 빌드
FROM openjdk:21-jdk-alpine AS builder

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle bootJar --no-daemon

# 2단계: 실행 이미지
FROM openjdk:21-jdk-alpine

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]