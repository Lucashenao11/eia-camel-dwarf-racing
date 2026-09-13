package com.example.camel_dwarf_racing_api.exception;

public class CompetitorNotOnTeamException extends RuntimeException {

    public CompetitorNotOnTeamException(Long competitorId, Long teamId) {
        super("Competitor with ID " + competitorId + " is not a member of team " + teamId + ".");
    }
}