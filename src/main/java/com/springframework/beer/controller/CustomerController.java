package com.springframework.beer.controller;

import com.springframework.beer.model.Customer;
import com.springframework.beer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @DeleteMapping("{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID customerId){
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("{customerId}")
    public ResponseEntity<Void> updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers(){
        return customerService.listCustomers();
    }

    @PostMapping
    public ResponseEntity<Customer> saveNewCustomer(@RequestBody Customer customer) {
        var savedCustomer = customerService.saveNewCustomer(customer);
        var httpHeader = new HttpHeaders();
        httpHeader.add("Location", "/api/v1/customer/"+savedCustomer.getId());
        return new ResponseEntity<>(savedCustomer, httpHeader, HttpStatus.CREATED);
    }
    @RequestMapping(value = "{customerId}",method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
        return customerService.getCustomerById(customerId);
    }

}
