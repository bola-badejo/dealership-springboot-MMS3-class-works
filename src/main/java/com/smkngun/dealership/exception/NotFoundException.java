package com.smkngun.dealership.exception;

// Thrown when a lookup by id finds nothing. A dedicated class makes it easy
// to map to HTTP 404 in one place (see GlobalExceptionHandler below).
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
