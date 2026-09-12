package com.example.camel_dwarf_racing_api.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.camel_dwarf_racing_api.model.TeamStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamResponseDto {

    private Long id;
    private String name;
    private String description;
    private String coach;
    private LocalDate creationDate;
    private TeamStatus status;
    private List<CompetitorResponseDto> competitors;
}