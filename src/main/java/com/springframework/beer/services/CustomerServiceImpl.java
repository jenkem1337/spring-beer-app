package com.springframework.beer.services;

import com.springframework.beer.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl(){
        this.customerMap = new HashMap<>();

        var c1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Hasancan")
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();
        var c2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Jackson")
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();
        var c3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Vladamir")
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(c1.getId(), c1);
        customerMap.put(c2.getId(), c2);
        customerMap.put(c3.getId(), c3);

    }
    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customerMap.get(id);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        var newCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        var existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(existingCustomer.getId(), existingCustomer);
    }
}
