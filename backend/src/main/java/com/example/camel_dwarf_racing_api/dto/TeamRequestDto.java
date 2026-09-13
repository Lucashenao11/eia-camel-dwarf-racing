package com.example.camel_dwarf_racing_api.dto;

import jakarta.validation.constraints.NotBlank;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamRequestDto {
    
    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    @NotBlank(message = "The name of the coach cannot be empty")
    private String coach;
}
