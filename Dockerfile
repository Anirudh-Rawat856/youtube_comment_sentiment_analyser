# ===============================
# YouTube Sentiment Analyzer - Dockerfile
# ===============================

# 1️⃣ Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml and dependencies first for efficient caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# 2️⃣ Runtime stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the app port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

