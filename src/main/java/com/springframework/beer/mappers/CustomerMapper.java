package com.springframework.beer.mappers;

import com.springframework.beer.entites.Customer;
import com.springframework.beer.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
