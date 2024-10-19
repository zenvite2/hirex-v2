FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY ../ .

RUN ls

RUN apt-get update -y && \
    apt-get upgrade -y

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/main/target/*.jar app.jar

EXPOSE 8080

# Define the default command to run when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
