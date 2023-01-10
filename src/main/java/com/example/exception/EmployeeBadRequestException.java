package com.example.exception;

public class EmployeeBadRequestException extends RuntimeException {
    
    public EmployeeBadRequestException(String message) {
        super(message);
    }
}
