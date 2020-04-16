FROM openjdk:8-jdk-slim

ARG APPLICATION_NAME
ARG APP_PORT

ENV APPLICATION ${APPLICATION_NAME}

ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2 -Djava.security.egd=file:/dev/./urandom"
ENV JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
ENV JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.rmi.port=1099"
ENV JAVA_OPTS="${JAVA_OPTS} -Djava.rmi.server.hostname=127.0.0.1"
ENV JAVA_OPTS="${JAVA_OPTS} -DEnableLogstash"

RUN mkdir -p /var/run/springData \
    && chmod -R 755 /var/run/springData \
    && mkdir -p /opt/springData/${APPLICATION_NAME} \
    && chmod -R 755 /opt/springData/${APPLICATION_NAME} \
    && mkdir -p /var/log/springData/${APPLICATION_NAME} \
    && chmod -R 755 /var/log/springData/${APPLICATION_NAME} \
    && mkdir -p /usr/bin \
    && chmod -R 755 /usr/bin \
    && ln -s /var/log/springData/${APPLICATION_NAME} /opt/springData/${APPLICATION_NAME}/log \
    && echo "ALL : ALL " >> /etc/hosts.allow \
    && echo "#!/bin/sh\nexec java ${JAVA_OPTS} -jar app.jar \"$@\"" > /opt/springData/${APPLICATION_NAME}/run.sh \
    && chmod 755 /opt/springData/${APPLICATION_NAME}/run.sh

WORKDIR /opt/springData/${APPLICATION_NAME}

EXPOSE ${APP_PORT}

COPY ./target/*-exec.jar ./app.jar

ENTRYPOINT ["./run.sh"]
