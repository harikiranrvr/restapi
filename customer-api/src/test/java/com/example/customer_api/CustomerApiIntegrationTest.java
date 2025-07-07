package com.example.customer_api;

import com.example.customer_api.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

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
} 