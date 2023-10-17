package com.some.example.repository;

import com.some.example.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainersUnitTest {
    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsByEmail() {
        //GIVEN
        //WHEN

        //THEN
    }

    @Test
    void existsById() {
        //GIVEN
        //WHEN

        //THEN
    }
}