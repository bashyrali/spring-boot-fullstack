package com.some.example.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.some.example.dto.CustomerRegistrationRequest;
import com.some.example.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIT {
    @Autowired
    private WebTestClient webTestClient;
    private static final String CUSTOMER_URI = "/api/v1/customers";
    private static final Random RANDOM = new Random();

    @Test
    void allCustomers() {
        //GIVEN
        //WHEN

        //THEN
    }

    @Test
    void canRegister() {
        // create registration
        int age = RANDOM.nextInt(1, 99);
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.fullName() + "-" + UUID.randomUUID() + "@mail.ru";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure customer is present
        Customer expected = new Customer(
                name, email, age
        );

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(expected.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expected.setId(id);
        //todo

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expected);
    }
}
