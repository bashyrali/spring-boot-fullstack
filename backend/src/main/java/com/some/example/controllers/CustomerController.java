package com.some.example.controllers;

import com.some.example.dto.CustomerRegistrationRequest;
import com.some.example.dto.CustomerUpdateRequest;
import com.some.example.entity.Customer;
import com.some.example.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> customers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customers")
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @GetMapping("/customers/{customerId}")
    public Customer customers(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @DeleteMapping("customers/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PutMapping("customers/{customerId}")
    public void updateCustomer(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}
