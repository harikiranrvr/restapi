# Testing the Customer API

This document explains how to run integration and acceptance tests for the Customer API, and how these tests are automated in CI/CD.

## Integration & Acceptance Testing Pattern

- Integration tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)` to start the full application and hit real HTTP endpoints.
- Tests use `TestRestTemplate` to simulate real client requests to the REST API.
- Example test: `CustomerApiIntegrationTest` covers POST and GET for `/customers`.

## How to Run Tests Locally

From the `customer-api` directory:

```sh
./mvnw test
```

- This runs all unit and integration tests.
- To run only integration tests (if you use a naming convention like `*IT.java`):
  ```sh
  ./mvnw verify -Dtest=*IT
  ```

## How It Works in CI/CD

- The GitHub Actions workflow runs `mvn clean verify` on every push and pull request.
- This ensures all tests (unit, integration, acceptance) are run automatically.
- If any test fails, the build will fail and the pipeline will stop.

## How to Expand

- Add more integration or acceptance tests in `src/test/java/com/example/customer_api/`.
- Use [Testcontainers](https://www.testcontainers.org/) for real database or service dependencies.
- For API contract or E2E testing, consider tools like Spring Cloud Contract, RestAssured, or Postman/Newman.

## Example: Add a New Test

Create a new test class in `src/test/java/com/example/customer_api/` and annotate with `@SpringBootTest(webEnvironment = RANDOM_PORT)`.

---

For questions or to expand the test suite, see the Spring Boot [testing documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing). 