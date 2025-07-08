# restapi
Backend mini project

## Quickstart

**Prerequisites:**
Absolutely! Here are the **prerequisites installation instructions for Java 11, Maven, and Docker** for **Windows, Mac, and Ubuntu/Linux**—formatted for direct copy-paste into your README:

---

## Prerequisites

### Java 11 or higher

- **Windows:**
  - Download the [OpenJDK 11 MSI installer](https://adoptium.net/temurin/releases/?version=11) (choose MSI for easy install).
  - Run the installer and follow the prompts.
  - (Optional) Set the `JAVA_HOME` environment variable to the JDK install path.
  - Verify installation:
     ```cmd
     java -version
     ```

- **Mac (Homebrew):**
  ```sh
  brew install openjdk@11
  ```
  Add to your shell profile (if needed):
  ```sh
  echo 'export PATH="/usr/local/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc
  source ~/.zshrc
  ```

- **Ubuntu/Linux:**
  ```sh
  sudo apt update
  sudo apt install openjdk-11-jdk
  java -version
  ```

---

### Maven 3.6+ (or use the included Maven wrapper)

- **Windows:**
  - Download the [Maven zip archive](https://maven.apache.org/download.cgi).
  - Extract to a folder (e.g., `C:\Program Files\Apache\maven`).
  - Add the `bin` directory to your `PATH` environment variable.
  - Verify installation:
     ```cmd
     mvn -version
     ```

- **Mac (Homebrew):**
  ```sh
  brew install maven
  mvn -version
  ```

- **Ubuntu/Linux:**
  ```sh
  sudo apt update
  sudo apt install maven
  mvn -version
  ```

---

### Docker (optional, for containerization)

- **Windows:**
 - Download and install [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/).
 - Start Docker Desktop.
 - Verify installation:
     ```cmd
     docker --version
     ```

- **Mac:**
  - Download and install [Docker Desktop for Mac](https://docs.docker.com/desktop/install/mac-install/).
  - Start Docker Desktop.
  - Verify installation:
     ```sh
     docker --version
     ```

- **Ubuntu/Linux:**
  ```sh
  sudo apt update
  sudo apt install \
    ca-certificates \
    curl \
    gnupg
  sudo install -m 0755 -d /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
  echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) stable" | \
    sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  sudo apt update
  sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
  docker --version
  ```

---
- For more details, see the official documentation for [Java](https://adoptium.net/), [Maven](https://maven.apache.org/install.html), and [Docker](https://docs.docker.com/get-docker/).
---
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

- **Health and Metrics:**  
  - `GET /actuator/health` — Health check endpoint  
  - `GET /actuator/metrics` — Application and JVM metrics  
  - `GET /actuator/httptrace` — Recent HTTP request traces

- **In-memory H2 Database Console:**  
  - (If enabled) `http://localhost:8080/h2-console`  
    Use for direct DB inspection during development.

### **How to Run**
See the [Quickstart](#quickstart) section above.

## Entity Schema

| Attribute     | Type   | Constraints         | Status |
|---------------|--------|--------------------|--------|
| Id            | UUID   | Primary Key        | ✅     |
| First Name    | String | Required           | ✅     |
| Middle Name   | String | Optional           | ✅     |
| Last Name     | String | Required           | ✅     |
| Email Address | String | Unique, Required   | ✅     |
| Phone Number  | String | Required           | ✅     |

## API Endpoints

- `GET /customers` - Get all customers
- `GET /customers/{id}` - Get customer by ID
- `GET /customers?email={email}` - Get customer by email
- `POST /customers` - Create new customer
- `PUT /customers/{id}` - Update customer (see known issues)
- `DELETE /customers/{id}` - Delete customer
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


## For Update and Delete Operation:
### Known Issues

Both the update and delete endpoints have a known JPA persistence context issue where the repository's `findById` and `existsById` methods cannot locate existing customers, even though they exist and can be found by the get-by-id endpoint and list endpoint. This appears to be related to transaction boundaries or entity state management in the Spring Data JPA configuration.

**Working Operations:**
- ✅ **Create Customer** - POST `/customers`
- ✅ **Read Customer by ID** - GET `/customers/{id}`
- ✅ **Read All Customers** - GET `/customers`
- ✅ **Read Customer by Email** - GET `/customers?email={email}`

**Workaround:** For now, updates and deletes can be performed by:
1. Using the list endpoint to get all customers
2. Creating new customers with updated data (for updates)
3. The delete operation would require a different approach or database-level operations

**Technical Details:**
- The get-by-id and list endpoints successfully find customers
- The update and delete endpoints' repository calls return empty/false for the same IDs
- This suggests a transaction or entity state management issue
- Would require deeper investigation of JPA configuration and transaction management
- The issue affects `findById()`, `existsById()`, and related repository methods in certain contexts

**Status:** Under investigation - JPA/Hibernate persistence context issue

## Technology Stack

- Java 11
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5
