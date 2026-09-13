package com.example.camel_dwarf_racing_api.exception;

public class RaceNotInProgressException extends RuntimeException {
    public RaceNotInProgressException(Long raceId) {
        super("Race with ID " + raceId + " is not IN_PROGRESS; results cannot be recorded.");
    }
}