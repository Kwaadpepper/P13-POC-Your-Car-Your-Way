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
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
