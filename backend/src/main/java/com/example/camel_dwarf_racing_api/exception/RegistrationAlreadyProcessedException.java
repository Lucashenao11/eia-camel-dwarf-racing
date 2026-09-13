package com.example.camel_dwarf_racing_api.exception;

public class RegistrationAlreadyProcessedException extends RuntimeException {
    public RegistrationAlreadyProcessedException(Long id) {
        super("Registration with ID " + id + " has already been processed and cannot be changed.");
    }
}