package com.some.example.service;

import com.some.example.dto.CustomerRegistrationRequest;
import com.some.example.dto.CustomerUpdateRequest;
import com.some.example.entity.Customer;
import com.some.example.exception.DuplicateResource;
import com.some.example.exception.ResourceNotFoundException;
import com.some.example.interfaces.CustomerDao;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        //GIVEN
        //WHEN
        underTest.getAllCustomers();

        //THEN
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById() {
        //GIVEN
        long id = 1;
        Customer customer = new Customer(
                id,
                "Name",
                "email@mail.ru",
                44
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //WHEN
        Customer actual = underTest.getCustomerById(id);
        //THEN
        verify(customerDao).selectCustomerById(id);
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerByIdThrowException() {
        //GIVEN
        long id = 1;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id %s not found");
    }

    @Test
    void addCustomer() {
        //GIVEN
        String email = "email@mail.ru";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name",
                email,
                22
        );
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        //WHEN
        underTest.addCustomer(request);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void addCustomerThrownException() {
        //GIVEN
        String email = "email@mail.ru";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name",
                email,
                22
        );
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        //WHEN
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResource.class)
                .hasMessage("Customer with Email already exist");

        //THEN
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //GIVEN
        long id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);
        //WHEN
        underTest.deleteCustomer(id);
        //THEN
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerThrownException() {
        //GIVEN
        long id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);
        //WHEN
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id %s not found");
        //THEN
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomerOnlyName() {
        //GIVEN
        long id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@mail.ru"
                , 22
        );
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "NewName",
                null,
                null
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //WHEN
        underTest.updateCustomer(id, request);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }
}