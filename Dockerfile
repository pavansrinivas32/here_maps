# Stage 1: Build the Maven project
FROM maven:3.8-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml /app/
COPY src /app/src/

# Run the Maven build (skip tests)
RUN mvn -f /app/pom.xml clean package -DskipTests

# Stage 2: Prepare the runtime environment
FROM openjdk:17.0.1-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file from the previous stage
COPY --from=build /app/target/loc-0.0.1-SNAPSHOT.jar /app/loc.jar

# Expose port 8080
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "loc.jar"]
