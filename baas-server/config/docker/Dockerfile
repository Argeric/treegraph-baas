FROM openjdk:11.0.9-jre
LABEL name="treegraph-baas" version="1.0" author="zt<zhenting@conflux-chain.org>"
VOLUME /opt/service/log
ARG JAR_FILE
COPY ${JAR_FILE} /opt/service/app.jar
EXPOSE 9797
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' > /etc/timezone && \
    cp /etc/apt/sources.list /etc/apt/sources.list.bak && \
    sed -i "s@http://deb.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list && \
    sed -i "s@http://security.debian.org@http://mirrors.aliyun.com@g" /etc/apt/sources.list && \
    apt-get update && \
    apt-get -y install openssh-client && \
    mkdir -p /root/.ssh
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/opt/service/app.jar"]