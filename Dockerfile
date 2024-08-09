#
# Build stage
#
FROM maven:3-openjdk-17 as build

WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-alpine

WORKDIR /opt

# Copy the JAR file from the build stage
COPY --from=build /app/target/art-cart-backend-0.0.1-SNAPSHOT.jar /opt/app.jar

# Expose the port
ENV PORT 9090
EXPOSE ${PORT}

# Run the JAR file
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "/opt/app.jar"]
