# Containerization Instructions

This document explains how to build and run the Docker container for the Spring Boot Customer API.

## Prerequisites
- Docker installed on your machine

## Build the Docker Image
From the `customer-api` directory, run:

```sh
docker build -t customer-api:latest .
```

## Run the Container
To run the container and expose it on port 8080:

```sh
docker run -p 8080:8080 customer-api:latest
```

## Environment Variables
- By default, the application uses the configuration in `src/main/resources/application.properties`.
- To override properties (e.g., database connection), you can pass environment variables or mount a custom properties file:

```sh
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb customer-api:latest
```

Or mount a custom config:

```sh
docker run -p 8080:8080 -v /path/to/custom/application.properties:/app/application.properties customer-api:latest
```

## Notes
- The default exposed port is `8080`.
- The application JAR is built as `customer-api-0.0.1-SNAPSHOT.jar` by Maven. 