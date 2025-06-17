FROM eclipse-temurin:17-jre-jammy as docker
WORKDIR /app

COPY target/*.jar app.jar

# Proper entrypoint for Spring Boot
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]