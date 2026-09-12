package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.CompetitorStatus;
import com.example.camel_dwarf_racing_api.model.CompetitorType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompetitorResponseDto {

    private Long id;
    private String name;
    private String nickname;
    private CompetitorType type;
    private LocalDate dateOfBirth;
    private Double weight;
    private Double height;
    private String countryOfOrigin;
    private CompetitorStatus status;
    private LocalDate registrationDate;
}