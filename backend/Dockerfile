FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} moodle-grades-master.jar

RUN addgroup -S mootse-master \
    && adduser -S mootse-master -G mootse-master

USER mootse-master

ENTRYPOINT ["java", "-jar", "/moodle-grades-master.jar"]