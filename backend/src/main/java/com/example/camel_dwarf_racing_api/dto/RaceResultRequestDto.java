package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.ResultStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceResultRequestDto {

    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    private Integer finalPosition; // required only if status is FINISHED

    private Double completionTimeSeconds; // required only if status is FINISHED

    private Double penaltyTimeSeconds;

    @NotNull(message = "Status is required")
    private ResultStatus status;

    private String notes;
}