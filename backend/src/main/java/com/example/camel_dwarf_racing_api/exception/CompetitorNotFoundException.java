package com.example.camel_dwarf_racing_api.exception;

public class CompetitorNotFoundException extends RuntimeException {
    public CompetitorNotFoundException(Long id){
        super("Competitor with ID " + id + " was not found");
    }
}
