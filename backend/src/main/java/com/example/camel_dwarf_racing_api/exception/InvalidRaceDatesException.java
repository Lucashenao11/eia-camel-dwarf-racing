package com.example.camel_dwarf_racing_api.exception;

public class InvalidRaceDatesException extends RuntimeException {
    public InvalidRaceDatesException(String message) {
        super(message);
    }
}