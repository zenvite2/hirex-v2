FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

#RUN apt-get update -y && \
#    apt-get upgrade -y

COPY pom.xml .
COPY main/pom.xml ./main/
COPY data/pom.xml ./data/
COPY websocket/pom.xml ./websocket/

RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/websocket/target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
