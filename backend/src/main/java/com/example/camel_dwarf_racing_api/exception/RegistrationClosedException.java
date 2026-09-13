package com.example.camel_dwarf_racing_api.exception;

public class RegistrationClosedException extends RuntimeException {
    public RegistrationClosedException(Long raceId) {
        super("Race with ID " + raceId + " is not open for registration.");
    }
}