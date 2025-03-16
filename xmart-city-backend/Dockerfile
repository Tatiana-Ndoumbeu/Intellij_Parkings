FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/xmart-zity-backend-1.0-SNAPSHOT.jar app.jar
EXPOSE 45065
CMD ["java", "-jar", "app.jar"]
