package com.some.example.dao;

import com.some.example.entity.Customer;
import com.some.example.interfaces.CustomerDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDao implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDao(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT *
                FROM customers
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT *
                FROM customers
                WHERE customers.id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customers(name, email, age)
                VALUES (?, ?, ?)
                """;
        int update = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT EXISTS(SELECT FROM customers WHERE customers.email = ?)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);

    }

    @Override
    public boolean existsPersonWithId(Long id) {
        var sql = """
                SELECT EXISTS(SELECT FROM customers WHERE customers.id = ?)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        var sql = """
                DELETE FROM customers
                WHERE customers.id = ?
                """;
        jdbcTemplate.update(sql, customerId);
    }

    @Override
    public void updateCustomer(Customer customer) {
        var sql = """
                UPDATE customers set name = ?, email = ?, age = ? 
                WHERE id =?
                """;
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge(), customer.getId());
    }
}
