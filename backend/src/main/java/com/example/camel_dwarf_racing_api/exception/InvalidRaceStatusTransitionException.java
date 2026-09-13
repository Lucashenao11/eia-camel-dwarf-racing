package com.example.camel_dwarf_racing_api.exception;

import com.example.camel_dwarf_racing_api.model.RaceStatus;

public class InvalidRaceStatusTransitionException extends RuntimeException {
    public InvalidRaceStatusTransitionException(RaceStatus from, RaceStatus to) {
        super("Cannot transition race from " + from + " to " + to + ".");
    }
}