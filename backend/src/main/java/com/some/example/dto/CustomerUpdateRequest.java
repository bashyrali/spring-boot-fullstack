package com.some.example.dto;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age) {
}
