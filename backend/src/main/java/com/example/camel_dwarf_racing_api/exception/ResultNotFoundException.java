package com.example.camel_dwarf_racing_api.exception;

public class ResultNotFoundException extends RuntimeException {
    public ResultNotFoundException(Long id) {
        super("Result with ID " + id + " was not found");
    }
}