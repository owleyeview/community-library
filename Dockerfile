# JDK base image
FROM eclipse-temurin:21-jdk-alpine as builder

LABEL authors="RashaanLightpool"
LABEL org.opencontainers.image.source=https://github.com/owleyeview/YOUR_REPO
LABEL org.opencontainers.image.description="Tool Library MVP container image"
LABEL org.opencontainers.image.licenses=MIT

# Set the working directory
WORKDIR /app

# Copy the current directory contents(maven files and source code) into the container at /app
COPY . /app

# Build the application
RUN ./mvnw clean package -DskipTests

# Start a new runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR file from the builder image to the runtime image
COPY --from=builder /app/target/*.jar /app/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file to start the application
ENTRYPOINT ["java","-jar","/app/app.jar"]