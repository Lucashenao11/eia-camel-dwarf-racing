package com.example.camel_dwarf_racing_api.exception;

public class DuplicateFinalPositionException extends RuntimeException {
    public DuplicateFinalPositionException(Long raceId, Integer position) {
        super("Race with ID " + raceId + " already has a finisher in position " + position + ".");
    }
}