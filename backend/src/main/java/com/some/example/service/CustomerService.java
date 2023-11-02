package com.some.example.service;

import com.some.example.dto.CustomerRegistrationRequest;
import com.some.example.dto.CustomerUpdateRequest;
import com.some.example.entity.Customer;
import com.some.example.exception.DuplicateResource;
import com.some.example.exception.NoChangeException;
import com.some.example.exception.ResourceNotFoundException;
import com.some.example.interfaces.CustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Long id) {
        return customerDao.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with id %s not found"));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        //Check email
        if (customerDao.existsPersonWithEmail(request.email())) {
            throw new DuplicateResource("Customer with Email already exist");
        }
        //Add customer
        customerDao.insertCustomer(new Customer(
                request.name(),
                request.email(),
                request.age()
        ));
    }

    public void deleteCustomer(Long id) {
        if (!customerDao.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("Customer with id %s not found");
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomerById(id);
        boolean changes = false;
        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if (updateRequest.email() != null && updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResource("Email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if (!changes) {
            throw new NoChangeException("No data changes found");
        }
        System.out.println(customer);
        customerDao.updateCustomer(customer);
    }
}
