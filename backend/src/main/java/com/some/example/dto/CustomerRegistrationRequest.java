package com.some.example.dto;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age) {
}
