package com.example.camel_dwarf_racing_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceRegistrationRequestDto {

    private Long competitorId; // exactly one of these two must be provided
    private Long teamId;
}