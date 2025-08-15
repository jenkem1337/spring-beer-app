package com.springframework.beer.services;

import com.springframework.beer.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl(){
        this.customerMap = new HashMap<>();

        var c1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Hasancan")
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();
        var c2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Jackson")
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();
        var c3 = CustomerDTO.builder()
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
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        var newCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName(customerDTO.getCustomerName())
                .createdDate(LocalDateTime.now())
                .version(1)
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        var existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(customerDTO.getCustomerName());
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        return Optional.of(existingCustomer);
    }

    @Override
    public boolean deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public Optional<CustomerDTO> patchUpdateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        var existing = customerMap.get(customerId);
        if(StringUtils.hasText(customerDTO.getCustomerName())){
            existing.setCustomerName(customerDTO.getCustomerName());
        }
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());
        return Optional.of(existing);
    }
}
