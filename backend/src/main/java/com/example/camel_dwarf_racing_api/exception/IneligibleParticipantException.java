package com.example.camel_dwarf_racing_api.exception;

public class IneligibleParticipantException extends RuntimeException {
    public IneligibleParticipantException(String message) {
        super(message);
    }
}