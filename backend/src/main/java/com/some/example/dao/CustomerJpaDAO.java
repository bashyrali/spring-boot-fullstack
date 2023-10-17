package com.some.example.dao;

import com.some.example.entity.Customer;
import com.some.example.interfaces.CustomerDao;
import com.some.example.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDAO implements CustomerDao {
    private final CustomerRepository customerRepository;

    public CustomerJpaDAO(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
