# Observability for Customer API

## Logging
- All application logs are output to stdout in structured format.
- Log levels can be configured via `application.properties`.
- Logs can be collected by ELK, Loki, or cloud logging services.

## Metrics
- Application and JVM metrics are available at `/actuator/metrics`.
- Compatible with Prometheus scraping for Kubernetes environments.

## Health Checks
- Liveness/readiness at `/actuator/health`.
- Use for Kubernetes probes.

## HTTP Tracing
- Recent HTTP requests are available at `/actuator/httptrace`.

## Distributed Tracing (Optional)
- Add Spring Cloud Sleuth for trace IDs in logs and HTTP headers.
- Compatible with Zipkin, Jaeger, or OpenTelemetry collectors.

## Enabling Actuator Endpoints

In `src/main/resources/application.properties`:
```
management.endpoints.web.exposure.include=health,info,metrics,httptrace
management.endpoint.health.show-details=always
```

## Assumptions
- Logs are collected from stdout by your container orchestrator or logging agent.
- Metrics are scraped by Prometheus or similar.
- Traces are sent to Zipkin/Jaeger if Sleuth is enabled. 