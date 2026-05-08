# Build stage
# Must be built with repo root as Docker build context:
#   docker build -f database-service/Dockerfile -t database-service:latest .
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

# Copy POMs first for better layer caching
COPY pom.xml ./
COPY database-core/pom.xml database-core/pom.xml
COPY database-service/pom.xml database-service/pom.xml
COPY backend/pom.xml backend/pom.xml

# Download dependencies for database-service and its inter-module dep (database-core)
RUN mvn -pl database-service -am dependency:go-offline -B -q

# Copy source (only modules needed for this service)
COPY database-core/src database-core/src
COPY database-service/src database-service/src

RUN mvn -pl database-service -am package -DskipTests -B -q

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /workspace/database-service/target/database-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
