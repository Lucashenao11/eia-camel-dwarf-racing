package com.example.camel_dwarf_racing_api.exception;

public class DuplicateTeamMembershipException extends RuntimeException {

    public DuplicateTeamMembershipException(Long teamiId, Long competitorId) {
        super("Competitor with ID '" + competitorId + "' is already a member of the team with ID '" + teamiId + "'");
    }
}