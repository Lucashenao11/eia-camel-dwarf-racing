package com.example.camel_dwarf_racing_api.exception;

public class TeamNotFoundException extends RuntimeException {
    
    public TeamNotFoundException(Long id) {
        super("Team with ID '" + id + "' was not found.");
    }
}
