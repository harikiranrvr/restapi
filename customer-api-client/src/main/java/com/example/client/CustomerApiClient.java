package com.example.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class CustomerApiClient {
    private static final String BASE_URL = "http://localhost:8080/customers";
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        CustomerApiClient client = new CustomerApiClient();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Customer API Client");
        while (true) {
            System.out.println("\nChoose an operation: list, get, create, update, delete, exit");
            String op = scanner.nextLine().trim().toLowerCase();
            switch (op) {
                case "list":
                    client.listCustomers();
                    break;
                case "get":
                    System.out.print("Enter customer ID: ");
                    String id = scanner.nextLine();
                    client.getCustomer(id);
                    break;
                case "create":
                    Customer c = new Customer();
                    System.out.print("First name: "); c.setFirstName(scanner.nextLine());
                    System.out.print("Middle name (optional): "); c.setMiddleName(scanner.nextLine());
                    System.out.print("Last name: "); c.setLastName(scanner.nextLine());
                    System.out.print("Email: "); c.setEmail(scanner.nextLine());
                    System.out.print("Phone number: "); c.setPhoneNumber(scanner.nextLine());
                    client.createCustomer(c);
                    break;
                case "update":
                    System.out.print("Enter customer ID: ");
                    String uid = scanner.nextLine();
                    Customer uc = new Customer();
                    System.out.print("First name: "); uc.setFirstName(scanner.nextLine());
                    System.out.print("Middle name (optional): "); uc.setMiddleName(scanner.nextLine());
                    System.out.print("Last name: "); uc.setLastName(scanner.nextLine());
                    System.out.print("Email: "); uc.setEmail(scanner.nextLine());
                    System.out.print("Phone number: "); uc.setPhoneNumber(scanner.nextLine());
                    client.updateCustomer(uid, uc);
                    break;
                case "delete":
                    System.out.print("Enter customer ID: ");
                    String did = scanner.nextLine();
                    client.deleteCustomer(did);
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Unknown operation.");
            }
        }
    }

    public void listCustomers() {
        ResponseEntity<Customer[]> resp = restTemplate.getForEntity(BASE_URL, Customer[].class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Arrays.stream(resp.getBody()).forEach(this::printCustomer);
        } else {
            System.out.println("Failed to list customers: " + resp.getStatusCode());
        }
    }

    public void getCustomer(String id) {
        try {
            ResponseEntity<Customer> resp = restTemplate.getForEntity(BASE_URL + "/" + id, Customer.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                printCustomer(resp.getBody());
            } else {
                System.out.println("Customer not found: " + resp.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createCustomer(Customer customer) {
        try {
            ResponseEntity<Customer> resp = restTemplate.postForEntity(BASE_URL, customer, Customer.class);
            if (resp.getStatusCode() == HttpStatus.CREATED && resp.getBody() != null) {
                System.out.println("Created:");
                printCustomer(resp.getBody());
            } else {
                System.out.println("Failed to create: " + resp.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateCustomer(String id, Customer customer) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);
            ResponseEntity<Customer> resp = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.PUT, entity, Customer.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                System.out.println("Updated:");
                printCustomer(resp.getBody());
            } else {
                System.out.println("Failed to update: " + resp.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteCustomer(String id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
            System.out.println("Deleted customer " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printCustomer(Customer c) {
        System.out.printf("ID: %s\nFirst: %s\nMiddle: %s\nLast: %s\nEmail: %s\nPhone: %s\n---\n",
                c.getId(), c.getFirstName(), c.getMiddleName(), c.getLastName(), c.getEmail(), c.getPhoneNumber());
    }
} 