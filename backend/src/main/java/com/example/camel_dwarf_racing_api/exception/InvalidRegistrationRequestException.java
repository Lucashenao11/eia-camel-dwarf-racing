package com.example.camel_dwarf_racing_api.exception;

public class InvalidRegistrationRequestException extends RuntimeException {
    public InvalidRegistrationRequestException(String message) {
        super(message);
    }
}