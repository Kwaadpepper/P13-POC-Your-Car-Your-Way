# 1. Build phase
FROM gradle:8-jdk AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

ARG SERVICE_MODULE
RUN ./gradlew :${SERVICE_MODULE}:bootJar -x test --no-daemon

# 2. Runtime phase
FROM eclipse-temurin:21-jre
WORKDIR /app

ARG SERVICE_MODULE_PATH
COPY --from=builder /home/gradle/project/${SERVICE_MODULE_PATH}/build/libs/*.jar app.jar

# Ajout du script d’attente générique
COPY docker/wait-for-service.sh /usr/local/bin/wait-for-service.sh
RUN chmod +x /usr/local/bin/wait-for-service.sh

EXPOSE 8080

ENV WAIT_HOST=
ENV WAIT_PORT=
ENV WAIT_TIMEOUT=180
ENV WAIT_INTERVAL=10
ENV WAIT_DELAYED_START=0

# Utilisation du script comme entrypoint
ENTRYPOINT ["sh", "-c", "wait-for-service.sh $WAIT_HOST $WAIT_PORT --timeout=$WAIT_TIMEOUT --wait=$WAIT_INTERVAL --delayed-start=$WAIT_DELAYED_START -- java -jar app.jar"]
