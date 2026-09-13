package com.example.camel_dwarf_racing_api.exception;

public class RegistrationNotFoundException extends RuntimeException {
    public RegistrationNotFoundException(Long id) {
        super("Registration with ID " + id + " was not found");
    }
}