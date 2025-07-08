package com.example.customer_api;

import com.example.customer_api.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndGetCustomer() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .phoneNumber("1234567890")
                .build();

        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/customers", customer, Customer.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Customer created = postResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getEmail()).isEqualTo("alice@example.com");

        // Get all customers
        ResponseEntity<Customer[]> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/customers", Customer[].class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).extracting(Customer::getEmail).contains("alice@example.com");
    }

    @Test
    void updateCustomer() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Bob")
                .lastName("Brown")
                .email("bob@example.com")
                .phoneNumber("5555555555")
                .build();
        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/customers", customer, Customer.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Customer created = postResponse.getBody();
        assertThat(created).isNotNull();

        // Update the customer
        created.setFirstName("Bobby");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> entity = new HttpEntity<>(created, headers);
        ResponseEntity<Customer> putResponse = restTemplate.exchange(
                "http://localhost:" + port + "/customers/" + created.getId(),
                HttpMethod.PUT, entity, Customer.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Customer updated = putResponse.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getFirstName()).isEqualTo("Bobby");
    }

    @Test
    void deleteCustomer() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Carol")
                .lastName("White")
                .email("carol@example.com")
                .phoneNumber("7777777777")
                .build();
        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/customers", customer, Customer.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Customer created = postResponse.getBody();
        assertThat(created).isNotNull();

        // Delete the customer
        restTemplate.delete("http://localhost:" + port + "/customers/" + created.getId());
        // Try to get the deleted customer
        ResponseEntity<Customer> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/customers/" + created.getId(), Customer.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findCustomerByEmail() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("David")
                .lastName("Green")
                .email("david@example.com")
                .phoneNumber("8888888888")
                .build();
        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/customers", customer, Customer.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Customer created = postResponse.getBody();
        assertThat(created).isNotNull();

        // Find by email
        ResponseEntity<Customer> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/customers?email=" + created.getEmail(), Customer.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Customer found = getResponse.getBody();
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("david@example.com");
    }

    @Test
    void createCustomerWithNullFirstName() {
        Customer customer = Customer.builder()
                .firstName(null) // Required field is null
                .lastName("NullFirst")
                .email("nullfirst@example.com")
                .phoneNumber("1111111111")
                .build();
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/customers", customer, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateCustomerWithNullLastName() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Frank")
                .lastName("Valid")
                .email("frank@example.com")
                .phoneNumber("2222222222")
                .build();
        ResponseEntity<Customer> postResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/customers", customer, Customer.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Customer created = postResponse.getBody();
        assertThat(created).isNotNull();

        // Try to update with null lastName
        created.setLastName(null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> entity = new HttpEntity<>(created, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(
                "http://localhost:" + port + "/customers/" + created.getId(),
                HttpMethod.PUT, entity, String.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findCustomerByNullEmail() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/customers?email=", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
} 