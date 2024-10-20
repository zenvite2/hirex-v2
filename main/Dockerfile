# Stage 1: Build the Maven project
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the pom.xml files to cache dependencies
COPY pom.xml .
COPY main/pom.xml ./main/
COPY data/pom.xml ./data/
COPY websocket/pom.xml ./websocket/

# Download the dependencies and cache them
RUN mvn dependency:go-offline

# Now copy the entire project
COPY . .

# Run the Maven build to package the 'main' module
RUN mvn clean package -DskipTests

# Stage 2: Create the final Docker image for the 'main' module
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the Spring Boot jar from the build stage (main module JAR)
COPY --from=build /app/main/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Define the default command to run when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
