# ---- Build Stage ----
FROM maven:3.8.8-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/customer-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 