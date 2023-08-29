FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine

ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} moodle-grades-master.jar

RUN addgroup -S moodle-master \
    && adduser -S moodle-master -G moodle-master \
    && mkdir -p /data \
    && chown moodle-master:moodle-master /data \
    && chmod 755 /data

USER moodle-master

ENTRYPOINT ["java", "-jar", "/moodle-grades-master.jar"]