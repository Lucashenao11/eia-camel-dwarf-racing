package com.example.camel_dwarf_racing_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandingEntryDto {

    private Long participantId;
    private String participantName;
    private String participantType; // "COMPETITOR" or "TEAM"
    private int points;
}