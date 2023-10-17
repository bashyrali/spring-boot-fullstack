package com.some.example;

import com.github.javafaker.Faker;
import com.some.example.entity.Customer;
import com.some.example.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication

public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            Random random = new Random();
            List<Customer> customers = List.of(
                    new Customer(faker.name().fullName(), faker.internet().safeEmailAddress(), random.nextInt(16, 99)),
                    new Customer(faker.name().fullName(), faker.internet().safeEmailAddress(), random.nextInt(16, 99)));
//            customerRepository.saveAll(customers);
        };
    }
}
