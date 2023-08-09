# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-oracle

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Spring Boot JAR file into the container
COPY build/libs/throttler-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]