# restapi
Backend mini project

## Quickstart

**Prerequisites:**
- Java 11 or higher
- Maven 3.6+ (or use the included Maven wrapper)
- (Optional) Docker, if you want to run the containerized version

**How to Run Locally:**
1. Clone the repository:
   ```sh
   git clone https://github.com/harikiranrvr/restapi.git
   cd restapi/customer-api
   ```
2. Build and start the API:
   ```sh
   ./mvnw spring-boot:run
   ```
   Or, if you have Maven installed:
   ```sh
   mvn spring-boot:run
   ```
3. The API will be available at:  
   `http://localhost:8080`

**Example Request:**
```sh
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Smith","email":"alice@example.com","phoneNumber":"1234567890"}'
```

---

# Customer API

## Progress

- Initialized a Spring Boot project (Maven, Java 17+, Spring Web, Spring Data JPA, H2, Lombok).
- Created a feature branch: `feature/customer-api`.
- Added the `Customer` entity with fields: UUID id, firstName, middleName, lastName, email (unique), phoneNumber.
- Used JPA annotations for ORM mapping and Lombok for boilerplate code reduction.

## Step 1: Build an API
### **Customer CRUD Operations**
- **Create a customer:**  
  `POST /customers`  
  Create a new customer.  
  **Body:** JSON with firstName, lastName, email, phoneNumber (middleName optional).

- **List all customers:**  
  `GET /customers`  
  Retrieve all customers.

- **Get a customer by ID:**  
  `GET /customers/{id}`  
  Retrieve a customer by their UUID.

- **Update a customer:**  
  `PUT /customers/{id}`  
  Update an existing customer’s details.  
  **Body:** JSON with updated fields.

- **Delete a customer:**  
  `DELETE /customers/{id}`  
  Remove a customer by their UUID.
  
### **Other API Features**
- **Find by email:**  
  `GET /customers?email={email}`  
  (If implemented) Retrieve a customer by their email address.

- **Health and Metrics:**  
  - `GET /actuator/health` — Health check endpoint  
  - `GET /actuator/metrics` — Application and JVM metrics  
  - `GET /actuator/httptrace` — Recent HTTP request traces

- **In-memory H2 Database Console:**  
  - (If enabled) `http://localhost:8080/h2-console`  
    Use for direct DB inspection during development.

### **How to Run**
See the [Quickstart](#quickstart) section above.

### **API Contract**
All requests and responses use JSON.  
The customer object structure:
```json
{
  "id": "uuid-string",
  "firstName": "string",
  "middleName": "string or null",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string"
}
```
For more details on endpoints and usage, see the [API Documentation](./customer-api/README.md) or use the `/actuator` endpoints for health and metrics.

---
## Step 2: Integration and/or Acceptance Testing
- The instructions for Integration and Acceptance Testing is mentioned here in this file [Testing](https://github.com/harikiranrvr/restapi/blob/main/customer-api/TESTING.md)

---
## Step 3: Provide Instrumentation of your API (Observability)
- The details related to Observability is mentioned here in this file [Observability](https://github.com/harikiranrvr/restapi/blob/main/customer-api/OBSERVABILITY.md)

---
## Step 4: Containerization
- Documentation for Containerization is here [Containerization](https://github.com/harikiranrvr/restapi/blob/main/customer-api/CONTAINER.md)

---
## Step 5: Kubernetes
- Details about Kubernetes setup is documented here [Kubernetes](https://github.com/harikiranrvr/restapi/blob/main/customer-api/KUBERNETES.md)

---
## Step 6: CI/CD
- Documentation for CD/CI is mentioned here [CD/CI](https://github.com/harikiranrvr/restapi/blob/main/customer-api/CI-CD.md)

---
## Step 7: App Integration
- New application is created and documentation for this step is mentioned here [customer-api-client](https://github.com/harikiranrvr/restapi/tree/main/customer-api-client)
