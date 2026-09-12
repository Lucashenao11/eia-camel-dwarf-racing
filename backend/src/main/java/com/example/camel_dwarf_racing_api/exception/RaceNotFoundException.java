package com.example.camel_dwarf_racing_api.exception;

public class RaceNotFoundException extends RuntimeException {
    public RaceNotFoundException(Long id) {
        super("Race with ID " + id + " was not found");
    }
}