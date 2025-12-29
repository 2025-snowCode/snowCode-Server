FROM eclipse-temurin:21-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.yml application.yml
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "app.jar"]