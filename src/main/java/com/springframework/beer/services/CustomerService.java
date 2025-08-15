package com.springframework.beer.services;

import com.springframework.beer.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);
    CustomerDTO saveNewCustomer(CustomerDTO customerDTO);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO);

    boolean deleteCustomerById(UUID customerId);

    Optional<CustomerDTO> patchUpdateCustomerById(UUID customerId, CustomerDTO customerDTO);
}
