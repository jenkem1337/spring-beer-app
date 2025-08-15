package com.springframework.beer.services;

import com.springframework.beer.mappers.CustomerMapper;
import com.springframework.beer.model.CustomerDTO;
import com.springframework.beer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    @Override
    public List<CustomerDTO> listCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(mapper.customerToCustomerDto(repository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        return mapper.customerToCustomerDto(repository.save(mapper.customerDtoToCustomer(customerDTO)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        repository.findById(customerId).ifPresentOrElse(presentCustomer -> {
            presentCustomer.setCustomerName(customerDTO.getCustomerName());
            presentCustomer.setLastModifiedDate(LocalDateTime.now());
            var savedCustomer = repository.save(presentCustomer);
            atomicReference.set(Optional.of(mapper.customerToCustomerDto(savedCustomer)));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public boolean deleteCustomerById(UUID customerId) {
        if(repository.existsById(customerId)){
            repository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchUpdateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        repository.findById(customerId).ifPresentOrElse(customer -> {
            if(StringUtils.hasText(customerDTO.getCustomerName())){
                customer.setCustomerName(customerDTO.getCustomerName());
            }
            customer.setLastModifiedDate(LocalDateTime.now());
            var savedCustomer = repository.save(customer);
            atomicReference.set(Optional.of(mapper.customerToCustomerDto(savedCustomer)));
        },() -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }
}
