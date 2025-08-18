package com.springframework.beer.controller;

import com.springframework.beer.mappers.CustomerMapper;
import com.springframework.beer.model.CustomerDTO;
import com.springframework.beer.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository repository;

    @Autowired
    CustomerMapper mapper;

    @Test
    void listCustomers() {
        var customer = customerController.listCustomers();
        assertThat(customer.size()).isEqualTo(3);
    }

    @Test
    void getCustomerById() {
        var customer = repository.findAll().getFirst();
        var dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }
    @Rollback
    @Transactional
    @Test
    void saveNewCustomer() {
        var dto = CustomerDTO.builder().customerName("Hasancan").build();
        var response = customerController.saveNewCustomer(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] splitLocationHeader = response.getHeaders().getLocation().getPath().split("/");
        UUID customerUUID = UUID.fromString( splitLocationHeader[4]);
        var savedCustomer = repository.findById(customerUUID);
        assertThat(savedCustomer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomerByIdAndFindById() {
        var customer = repository.findAll().getFirst();
        var customerDto = mapper.customerToCustomerDto(customer);
        customerDto.setId(null);
        customerDto.setVersion(null);
        final String newCustomerName = "Ali";
        customerDto.setCustomerName(newCustomerName);

        var response = customerController.updateCustomerById(customer.getId(), customerDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updatedCustomer = customerController.getCustomerById(customer.getId());
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo("Ali");
    }

    @Test
    void updateCustomerByIdNotFoundException() {
        assertThrows(NotFoundException.class, ()-> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteCustomerById(){
        var customer = repository.findAll().getFirst();
        var response = customerController.deleteCustomerById(customer.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(repository.existsById(customer.getId())).isFalse();
    }

    @Test
    void deleteCustomerByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }
//    @Test
//    void deleteCustomerByIdReturn404HttpStatus(){
//        assertThrows(NotFoundException.class, () -> {
//            var response = customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
//        });
//    }

    @Rollback
    @Transactional
    @Test
    void patchUpdateCustomerById() {
        var customer = repository.findAll().getFirst();
        var customerDto = mapper.customerToCustomerDto(customer);
        customerDto.setId(null);
        customerDto.setVersion(null);
        final String newCustomerName = "Ali";
        customerDto.setCustomerName(newCustomerName);

        var response = customerController.patchUpdateCustomerById(customer.getId(), customerDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updatedCustomer = customerController.getCustomerById(customer.getId());
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo("Ali");
    }

    @Test
    void patchUpdateCustomerByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            customerController.patchUpdateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }
}