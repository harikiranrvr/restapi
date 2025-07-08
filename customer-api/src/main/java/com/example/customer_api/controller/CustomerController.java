package com.example.customer_api.controller;

import com.example.customer_api.entity.Customer;
import com.example.customer_api.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer saved = customerRepository.save(customer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") UUID id) {
        logger.info("Requested ID: {}", id);
        logger.info("All IDs in DB: {}", customerRepository.findAll().stream().map(Customer::getId).collect(Collectors.toList()));
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = "email")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam("email") String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return customerRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") UUID id, @Valid @RequestBody Customer updatedCustomer) {
        logger.info("Update requested for ID: {}", id);
        logger.info("Update data: firstName={}, lastName={}, email={}", 
                   updatedCustomer.getFirstName(), updatedCustomer.getLastName(), updatedCustomer.getEmail());
        
        try {
            // Try to find the existing customer
            Customer existing = customerRepository.findById(id).orElse(null);
            
            if (existing != null) {
                logger.info("Found existing customer: firstName={}, lastName={}, email={}", 
                           existing.getFirstName(), existing.getLastName(), existing.getEmail());
                
                // Update the existing customer's fields
                existing.setFirstName(updatedCustomer.getFirstName());
                existing.setMiddleName(updatedCustomer.getMiddleName());
                existing.setLastName(updatedCustomer.getLastName());
                existing.setEmail(updatedCustomer.getEmail());
                existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
                
                // Save the updated customer
                Customer saved = customerRepository.save(existing);
                logger.info("Successfully updated customer with ID: {}, firstName={}, lastName={}, email={}", 
                           saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail());
                
                return ResponseEntity.ok(saved);
            } else {
                logger.info("Customer not found for ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") UUID id) {
        logger.info("Delete requested for ID: {}", id);
        logger.info("All IDs in DB: {}", customerRepository.findAll().stream().map(Customer::getId).collect(Collectors.toList()));
        
        boolean exists = customerRepository.existsById(id);
        logger.info("Customer exists: {}", exists);
        
        if (exists) {
            customerRepository.deleteById(id);
            logger.info("Customer deleted successfully");
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Customer not found for deletion");
            return ResponseEntity.notFound().build();
        }
    }
} 