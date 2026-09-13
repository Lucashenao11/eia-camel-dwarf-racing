package com.example.camel_dwarf_racing_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRejectionDto {

    @NotBlank(message = "A reason is required to reject a registration")
    private String reason;
}