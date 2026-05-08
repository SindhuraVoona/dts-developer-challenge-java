# Build stage
# Must be built with repo root as Docker build context:
#   docker build -f backend/Dockerfile -t backend-service:latest .
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

# Copy POMs first for better layer caching — these change less often than source
COPY pom.xml ./
COPY database-core/pom.xml database-core/pom.xml
COPY database-service/pom.xml database-service/pom.xml
COPY backend/pom.xml backend/pom.xml

# Download dependencies for backend and its inter-module deps (database-core)
RUN mvn -pl backend -am dependency:go-offline -B -q

# Copy source (only modules needed for the backend build)
COPY database-core/src database-core/src
COPY backend/src backend/src

# Build — skips database-service since it is not a dependency of backend
RUN mvn -pl backend -am package -DskipTests -B -q

# Runtime stage — slim JRE only
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as non-root for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /workspace/backend/target/backend-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
