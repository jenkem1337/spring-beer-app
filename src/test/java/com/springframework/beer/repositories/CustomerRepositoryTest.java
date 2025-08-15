package com.springframework.beer.repositories;

import com.springframework.beer.entites.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveNewCustomer() {
        var savedCustomer = customerRepository.save(
                Customer.builder().customerName("Hasancan").build()
        );
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getCustomerName()).isEqualTo("Hasancan");
    }
}