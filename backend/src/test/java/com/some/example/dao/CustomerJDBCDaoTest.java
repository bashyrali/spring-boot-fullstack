package com.some.example.dao;

import com.github.javafaker.Faker;
import com.some.example.AbstractTestContainersUnitTest;
import com.some.example.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDaoTest extends AbstractTestContainersUnitTest {
    private CustomerJDBCDao underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDao(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        //WHEN
        List<Customer> customers = underTest.selectAllCustomers();
        //THEN

        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //WHEN
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c ->
        {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //GIVEN
        long id = -1;

        //WHEN
        var actual = underTest.selectCustomerById(id);
        //THEN
        assertThat(actual).isEmpty();
    }

//    @Test
//    void insertCustomer() {
//        //GIVEN
//        Customer customer = new Customer(
//                FAKER.name().fullName(),
//                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
//                20
//        );
//        //WHEN
//        underTest.insertCustomer(customer);
//        Customer actual = underTest.selectCustomerById();
//
//        //THEN
//        assertThat()
//    }

    @Test
    void existsPersonWithEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        //WHEN
        boolean actual = underTest.existsPersonWithEmail(email);
        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnFalseDoesNotExists() {
        //GIVEN
        String email = FAKER.name().fullName() + "-" + UUID.randomUUID();
        //WHEN
        boolean actual = underTest.existsPersonWithEmail(email);
        //THEN
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        boolean actual = underTest.existsPersonWithId(id);
        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnFalseDoesNotExists() {
        //GIVEN
        long id = -1;
        //WHEN
        boolean actual = underTest.existsPersonWithId(id);
        //THEN
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        underTest.deleteCustomerById(id);
        var actual = underTest.selectCustomerById(id);
        //THEN
        assertThat(actual).isEmpty();
    }

    @Test
    void updateCustomerName() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newName = "NewName";

        //WHEN
        var update = underTest.selectCustomerById(id).get();
        update.setName(newName);
        underTest.updateCustomer(update);
        //THEN
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newEmail = "email@mai.ru";

        //WHEN
        var update = underTest.selectCustomerById(id).get();
        update.setEmail(newEmail);
        underTest.updateCustomer(update);
        //THEN
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        int newAge = 100;

        //WHEN
        var update = underTest.selectCustomerById(id).get();
        update.setAge(newAge);
        underTest.updateCustomer(update);
        //THEN
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }
 @Test
    void updateCustomer() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        var update = underTest.selectCustomerById(id).get();
        update.setName("NewName");
        update.setAge(55);
        update.setEmail(FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID());
        underTest.updateCustomer(update);
        //THEN
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }
}