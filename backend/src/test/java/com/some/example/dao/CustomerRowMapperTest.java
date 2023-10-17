package com.some.example.dao;

import com.some.example.entity.Customer;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //GIVEN
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Alex");
        when(resultSet.getString("email")).thenReturn("alex@mail.ru");
        //WHEN
        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        //THEN
        Customer expected = new Customer(
                1L,
                "Alex",
                "alex@mail.ru",
                19
        );
        assertThat(actual).isEqualTo(expected);
    }
}