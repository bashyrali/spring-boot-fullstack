package com.some.example.dao;

import com.some.example.entity.Customer;
import com.some.example.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJpaDAOTest {

    private CustomerJpaDAO underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDAO(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //GIVEN

        //WHEN
        underTest.selectAllCustomers();
        //THEN
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //GIVEN
        long id = 1;

        //WHEN
        underTest.selectCustomerById(id);
        //THEN
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //GIVEN
        Customer customer = new Customer();
        //WHEN
        underTest.insertCustomer(customer);
        //THEN
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //GIVEN
        //WHEN

        //THEN
    }

    @Test
    void existsPersonWithId() {
        //GIVEN
        //WHEN

        //THEN
    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        //WHEN

        //THEN
    }

    @Test
    void updateCustomer() {
        //GIVEN
        //WHEN

        //THEN
    }
}