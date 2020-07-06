FROM openjdk:8-jdk-alpine

MAINTAINER zt<zhenting@conflux-chain.org>

VOLUME /tmp

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

EXPOSE 9797

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]