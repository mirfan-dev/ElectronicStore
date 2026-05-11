# Build stage - compiles your Java code
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Run stage - uses the correct Java 17 image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]