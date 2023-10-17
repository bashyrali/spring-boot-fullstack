package com.some.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(name = "customer_email_unique", columnNames = "email")
})
@Data
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customer_id_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(
            nullable = false
    )
    private String email;

    @Column(nullable = false)
    private Integer age;

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;

    }

    public Customer(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;

    }

    public Customer() {

    }
}
