FROM eclipse-temurin:17-jre-jammy as docker
WORKDIR /app

# Copy only the built auth-service JAR
COPY target/*.jar app.jar

# Proper entrypoint for Spring Boot
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]