FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN chmod +x ./gradlew  # 실행 권한 부여
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]