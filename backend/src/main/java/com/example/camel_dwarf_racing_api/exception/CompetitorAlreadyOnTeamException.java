package com.example.camel_dwarf_racing_api.exception;

public class CompetitorAlreadyOnTeamException extends RuntimeException {

    public CompetitorAlreadyOnTeamException(Long competitorId) {
        super("Competitor with ID " + competitorId + " already belongs to a team.");
    }
}