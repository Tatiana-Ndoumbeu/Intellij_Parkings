# Use an official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory into the container
COPY target/xmart-city-backend-1.0-SNAPSHOT.jar /app/xmart-city-backend-1.0-SNAPSHOT.jar

# Expose the port (adjust according to your app's requirements)
EXPOSE 8080

# Command to run the JAR file
CMD ["java", "-jar", "xmart-city-backend-1.0-SNAPSHOT.jar"]
