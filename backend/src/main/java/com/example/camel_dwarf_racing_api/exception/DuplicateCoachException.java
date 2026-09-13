package com.example.camel_dwarf_racing_api.exception;

public class DuplicateCoachException extends RuntimeException {
    
    public DuplicateCoachException(String name) {
        super("Coach '" + name + "' already leads a team.");
    }
}
