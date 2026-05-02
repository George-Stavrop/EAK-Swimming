# Stage 1
FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR application
COPY target/gatewayserver-0.0.1-SNAPSHOT.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

# Stage 2
FROM eclipse-temurin:21-jre-alpine
LABEL PROJECT_NAME=gatewayserver

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR application

#Layered Copy
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "org.springframework.boot.loader.launch.JarLauncher"]