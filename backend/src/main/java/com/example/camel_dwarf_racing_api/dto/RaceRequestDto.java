package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.RaceType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RaceRequestDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Scheduled date/time is required")
    @Future(message = "A race cannot be scheduled in the past")
    private LocalDateTime scheduledDateTime;

    @NotBlank(message = "Start location is required")
    private String startLocation;

    @NotBlank(message = "Finish location is required")
    private String finishLocation;

    @Positive(message = "Distance must be greater than zero")
    private Double distanceMeters;

    @Positive(message = "Maximum participants must be greater than zero")
    private Integer maxParticipants;

    @NotNull(message = "Race type is required")
    private RaceType type;

    @NotBlank(message = "Organizer is required")
    private String organizer;

    @NotNull(message = "Registration deadline is required")
    private LocalDateTime registrationDeadline;
}