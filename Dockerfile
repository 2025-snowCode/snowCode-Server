FROM eclipse-temurin:21-jdk

RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/* \

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.yml application.yml
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "app.jar"]