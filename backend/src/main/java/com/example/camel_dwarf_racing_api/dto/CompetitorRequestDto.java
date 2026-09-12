package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.CompetitorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompetitorRequestDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Nickname cannot be empty")
    private String nickname;

    @NotNull(message = "Competitor type is required")
    private CompetitorType type;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Positive(message = "Weight must be positive")
    private Double weight;

    @Positive(message = "Height must be positive")
    private Double height;

    @NotBlank(message = "Country of origin cannot be empty")
    private String countryOfOrigin;
}