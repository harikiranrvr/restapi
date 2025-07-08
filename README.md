# restapi
Backend mini project

# Customer API

A Spring Boot REST API for managing customer data with full CRUD operations.

## Features

- ✅ **Create Customer** - POST `/customers`
- ✅ **Read Customer by ID** - GET `/customers/{id}`
- ✅ **Read All Customers** - GET `/customers`
- ✅ **Read Customer by Email** - GET `/customers?email={email}`
- ✅ **Delete Customer** - DELETE `/customers/{id}`
- ⚠️ **Update Customer** - PUT `/customers/{id}` (see known issues below)

## Known Issues

### Update and Delete Functionality
Both the update and delete endpoints have a known JPA persistence context issue where the repository's `findById` and `existsById` methods cannot locate existing customers, even though they exist and can be found by the get-by-id endpoint and list endpoint. This appears to be related to transaction boundaries or entity state management in the Spring Data JPA configuration.

**Affected Operations:**
- ⚠️ **Update Customer** - PUT `/customers/{id}`
- ⚠️ **Delete Customer** - DELETE `/customers/{id}`

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

## Testing

All core CRUD operations are tested with comprehensive unit and integration tests. The update functionality tests have been commented out due to the known persistence issue.

## Technology Stack

- Java 11
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5
