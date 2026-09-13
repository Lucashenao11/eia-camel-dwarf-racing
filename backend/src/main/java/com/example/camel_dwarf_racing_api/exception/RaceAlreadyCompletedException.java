package com.example.camel_dwarf_racing_api.exception;

public class RaceAlreadyCompletedException extends RuntimeException {
    public RaceAlreadyCompletedException(Long id) {
        super("Race with ID " + id + " is already completed and cannot be edited.");
    }
}