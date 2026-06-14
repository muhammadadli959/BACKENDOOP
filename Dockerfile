# Multi-stage Dockerfile for building and running the Spring Boot backend
# Build stage
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /workspace

# copy only the files needed to build to leverage Docker layer caching
COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests package

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy the built jar from the build stage
COPY --from=build /workspace/target/*.jar app.jar

# Render uses the PORT env var; Spring Boot needs server.port set
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]
