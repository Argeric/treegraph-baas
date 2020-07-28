FROM openjdk:8-jdk-alpine

LABEL name="treegraph-baas" version="1.0" author="zt<zhenting@conflux-chain.org>"

VOLUME /opt/service/log

ARG JAR_FILE

COPY ${JAR_FILE} /opt/service/app.jar

EXPOSE 9797

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/opt/service/app.jar"]