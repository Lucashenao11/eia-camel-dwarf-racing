package com.example.camel_dwarf_racing_api.exception;

public class DuplicateResultException extends RuntimeException {
    public DuplicateResultException(Long registrationId) {
        super("Registration with ID " + registrationId + " already has a recorded result.");
    }
}