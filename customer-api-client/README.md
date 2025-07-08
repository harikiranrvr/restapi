# Customer API Command-Line Client

This is a Java 11 command-line application that consumes the Customer REST API.

## Build
cd restapi/customer-api-client
From the `customer-api-client` directory:

```sh
mvn clean package
```

## Run

```sh
java -cp target/customer-api-client-0.0.1-SNAPSHOT.jar com.example.client.CustomerApiClient
```

## Usage

- The app will prompt for operations: `list`, `get`, `create`, `update`, `delete`, `exit`.
- For `create` and `update`, you will be prompted for all customer fields.
- The app expects the Customer API to be running at `http://localhost:8080/customers`.

## API Contract

- The client expects the following JSON structure for a Customer:
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
- All requests and responses use this contract.
- The client will print all results to stdout.

## Testing

- You can run the client against a local or remote instance of the Customer API.
- For automated tests, add JUnit tests in `src/test/java`. 
