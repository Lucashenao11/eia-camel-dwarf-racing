package com.example.camel_dwarf_racing_api.exception;

public class DuplicateTeamNameException extends RuntimeException {
    
    public DuplicateTeamNameException(String name) {
        super("A team with name '" + name + "' already exists.");
    }
}
