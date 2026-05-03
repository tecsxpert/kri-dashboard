# ──────────────────────────────────────────────
# Stage 1: Build the JAR
# Day 8 — Containerization (Java Developer 2)
# ──────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml first (layer-cache dependencies)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -B

# ──────────────────────────────────────────────
# Stage 2: Run the JAR (slim JRE image)
# ──────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Varad (Java Developer 2)"
LABEL description="KRI Dashboard Backend"

WORKDIR /app

# Copy built artifact from build stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
