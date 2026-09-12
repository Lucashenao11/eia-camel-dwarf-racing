package com.example.camel_dwarf_racing_api.exception;

public class TeamCapacityExceededException extends RuntimeException {
    
    public TeamCapacityExceededException(Long teamId, int maxTeamMembers) {
        super("The team with ID '" + teamId + "' has reached its max members capacity of " + maxTeamMembers);
    }
}
