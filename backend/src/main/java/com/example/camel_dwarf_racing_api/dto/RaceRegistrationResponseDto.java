package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.RegistrationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RaceRegistrationResponseDto {

    private Long id;
    private Long raceId;
    private String raceName;
    private Long competitorId;
    private String competitorNickname;
    private Long teamId;
    private String teamName;
    private LocalDateTime registrationDate;
    private RegistrationStatus status;
    private Integer startingPosition;
    private String validationNotes;
}