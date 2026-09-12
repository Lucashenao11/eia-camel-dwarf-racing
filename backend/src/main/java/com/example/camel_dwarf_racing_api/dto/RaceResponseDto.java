package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.RaceStatus;
import com.example.camel_dwarf_racing_api.model.RaceType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RaceResponseDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime scheduledDateTime;
    private String startLocation;
    private String finishLocation;
    private Double distanceMeters;
    private Integer maxParticipants;
    private RaceType type;
    private RaceStatus status;
    private String organizer;
    private LocalDateTime registrationDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}