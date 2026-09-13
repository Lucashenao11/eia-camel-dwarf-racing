package com.example.camel_dwarf_racing_api.exception;

public class InsufficientParticipantsException extends RuntimeException {
    public InsufficientParticipantsException(Long raceId, long currentCount) {
        super("Race with ID " + raceId + " needs at least 2 approved participants to start (currently has " + currentCount + ").");
    }
}