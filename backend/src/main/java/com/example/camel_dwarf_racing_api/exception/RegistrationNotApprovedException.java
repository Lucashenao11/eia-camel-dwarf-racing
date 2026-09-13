package com.example.camel_dwarf_racing_api.exception;

public class RegistrationNotApprovedException extends RuntimeException {
    public RegistrationNotApprovedException(Long registrationId) {
        super("Registration with ID " + registrationId + " is not APPROVED; it cannot receive a result.");
    }
}