package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.ResultStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RaceResultResponseDto {

    private Long id;
    private Long registrationId;
    private String participantName;
    private Integer startingPosition;
    private Integer finalPosition;
    private Double completionTimeSeconds;
    private Double penaltyTimeSeconds;
    private ResultStatus status;
    private String notes;
    private LocalDateTime recordedAt;
}