    FROM gradle:jdk17 AS builder
    WORKDIR /app
    COPY . .
    RUN gradle clean build --no-daemon

    FROM eclipse-temurin:17-jdk-alpine
    COPY --from=builder /app/build/libs/translate-interface-0.0.1-SNAPSHOT.jar translate.jar
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "translate.jar"]