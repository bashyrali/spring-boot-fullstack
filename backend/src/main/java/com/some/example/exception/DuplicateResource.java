package com.some.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResource extends RuntimeException {
    public DuplicateResource(String message) {
        super(message);
    }
}
