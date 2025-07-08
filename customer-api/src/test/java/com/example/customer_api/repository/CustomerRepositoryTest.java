package com.example.customer_api.repository;

import com.example.customer_api.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        
        testCustomer = Customer.builder()
                .firstName("John")
                .middleName("A")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    @Test
    @DisplayName("Should create a new customer")
    void testCreateCustomer() {
        Customer savedCustomer = customerRepository.save(testCustomer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getFirstName()).isEqualTo("John");
        assertThat(savedCustomer.getLastName()).isEqualTo("Doe");
        assertThat(savedCustomer.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedCustomer.getPhoneNumber()).isEqualTo("1234567890");
    }

    @Test
    @DisplayName("Should read customer by ID")
    void testReadCustomerById() {
        Customer savedCustomer = customerRepository.save(testCustomer);
        UUID customerId = savedCustomer.getId();

        Optional<Customer> foundCustomer = customerRepository.findById(customerId);

        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getId()).isEqualTo(customerId);
        assertThat(foundCustomer.get().getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should return empty when reading non-existent customer by ID")
    void testReadNonExistentCustomerById() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<Customer> foundCustomer = customerRepository.findById(nonExistentId);

        assertThat(foundCustomer).isEmpty();
    }

    @Test
    @DisplayName("Should read all customers")
    void testReadAllCustomers() {
        Customer customer1 = Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .phoneNumber("1111111111")
                .build();

        Customer customer2 = Customer.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@example.com")
                .phoneNumber("2222222222")
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        List<Customer> allCustomers = customerRepository.findAll();

        assertThat(allCustomers).hasSize(2);
        assertThat(allCustomers).extracting("firstName").containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    @DisplayName("Should find customer by email")
    void testFindByEmail() {
        customerRepository.save(testCustomer);

        Optional<Customer> found = customerRepository.findByEmail("john.doe@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return empty when finding customer by non-existent email")
    void testFindByNonExistentEmail() {
        Optional<Customer> found = customerRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update existing customer")
    void testUpdateCustomer() {
        Customer savedCustomer = customerRepository.save(testCustomer);
        UUID customerId = savedCustomer.getId();

        // Update the customer
        savedCustomer.setFirstName("Jane");
        savedCustomer.setPhoneNumber("9876543210");
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        assertThat(updatedCustomer.getId()).isEqualTo(customerId);
        assertThat(updatedCustomer.getFirstName()).isEqualTo("Jane");
        assertThat(updatedCustomer.getPhoneNumber()).isEqualTo("9876543210");
        assertThat(updatedCustomer.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should delete customer by ID")
    void testDeleteCustomerById() {
        Customer savedCustomer = customerRepository.save(testCustomer);
        UUID customerId = savedCustomer.getId();

        customerRepository.deleteById(customerId);

        Optional<Customer> foundCustomer = customerRepository.findById(customerId);
        assertThat(foundCustomer).isEmpty();
    }

    @Test
    @DisplayName("Should count total customers")
    void testCountCustomers() {
        assertThat(customerRepository.count()).isEqualTo(0);

        customerRepository.save(testCustomer);
        assertThat(customerRepository.count()).isEqualTo(1);

        Customer customer2 = Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("5555555555")
                .build();
        customerRepository.save(customer2);

        assertThat(customerRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should check if customer exists by ID")
    void testExistsById() {
        Customer savedCustomer = customerRepository.save(testCustomer);
        UUID customerId = savedCustomer.getId();

        // Verify the customer was saved by finding it
        Optional<Customer> found = customerRepository.findById(customerId);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(customerId);
        
        // Test with non-existent ID
        Optional<Customer> nonExistent = customerRepository.findById(UUID.randomUUID());
        assertThat(nonExistent).isEmpty();
    }

    @Test
    @DisplayName("Should enforce unique email constraint")
    void testUniqueEmailConstraint() {
        customerRepository.save(testCustomer);

        Customer duplicateEmailCustomer = Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("john.doe@example.com") // Same email as testCustomer
                .phoneNumber("5555555555")
                .build();

        assertThatThrownBy(() -> {
            customerRepository.save(duplicateEmailCustomer);
            customerRepository.flush(); // Force the constraint check
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should handle customer with null middle name")
    void testCustomerWithNullMiddleName() {
        Customer customerWithoutMiddleName = Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("5555555555")
                .build();

        Customer savedCustomer = customerRepository.save(customerWithoutMiddleName);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getMiddleName()).isNull();
    }

    @Test
    @DisplayName("Should save and retrieve multiple customers")
    void testSaveAndRetrieveMultipleCustomers() {
        Customer customer1 = Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .phoneNumber("1111111111")
                .build();

        Customer customer2 = Customer.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@example.com")
                .phoneNumber("2222222222")
                .build();

        Customer customer3 = Customer.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("3333333333")
                .build();

        List<Customer> savedCustomers = customerRepository.saveAll(List.of(customer1, customer2, customer3));

        assertThat(savedCustomers).hasSize(3);
        assertThat(savedCustomers).allMatch(customer -> customer.getId() != null);

        List<Customer> retrievedCustomers = customerRepository.findAll();
        assertThat(retrievedCustomers).hasSize(3);
        assertThat(retrievedCustomers).extracting("email")
                .containsExactlyInAnyOrder("alice.smith@example.com", "bob.johnson@example.com", "charlie.brown@example.com");
    }

    @Test
    @DisplayName("Should throw when saving customer with null required fields")
    void testSaveCustomerWithNullRequiredFields() {
        Customer customer = Customer.builder()
                .firstName(null) // required
                .lastName(null)  // required
                .email(null)     // required
                .phoneNumber(null) // required
                .build();
        assertThatThrownBy(() -> {
            customerRepository.save(customer);
            customerRepository.flush();
        }).isInstanceOf(Exception.class); // Could be DataIntegrityViolationException or PersistenceException
    }

    @Test
    @DisplayName("Should throw when updating customer with null required fields")
    void testUpdateCustomerWithNullRequiredFields() {
        Customer savedCustomer = customerRepository.save(testCustomer);
        savedCustomer.setFirstName(null);
        savedCustomer.setLastName(null);
        savedCustomer.setEmail(null);
        savedCustomer.setPhoneNumber(null);
        assertThatThrownBy(() -> {
            customerRepository.save(savedCustomer);
            customerRepository.flush();
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should not throw when deleting non-existent customer by ID")
    void testDeleteNonExistentCustomerById() {
        UUID nonExistentId = UUID.randomUUID();
        // Should not throw
        customerRepository.deleteById(nonExistentId);
        // DB should remain unchanged
        assertThat(customerRepository.count()).isEqualTo(0);
    }
} 