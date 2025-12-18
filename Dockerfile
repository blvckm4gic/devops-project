# =========================
# BUILD STAGE
# =========================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and config first (for layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build application
RUN ./gradlew clean build --no-daemon


# =========================
# RUNTIME STAGE
# =========================
FROM eclipse-temurin:17-jre-alpine

# Create non-root user
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/build/libs/todo-0.0.1-SNAPSHOT.jar app.jar

RUN chown -R app:app /app

USER app

EXPOSE 8080

# Healthcheck via Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]
