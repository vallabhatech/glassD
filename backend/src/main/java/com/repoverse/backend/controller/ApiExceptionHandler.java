package com.repoverse.backend.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleValidation(ConstraintViolationException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        return response(HttpStatus.BAD_GATEWAY, "Unable to build the ecosystem right now.");
    }

    private org.springframework.http.ResponseEntity<Map<String, Object>> response(HttpStatus status, String message) {
        return org.springframework.http.ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "timestamp", Instant.now().toString()));
    }
}
