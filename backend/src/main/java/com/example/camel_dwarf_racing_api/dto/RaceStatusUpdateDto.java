package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.RaceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceStatusUpdateDto {

    @NotNull(message = "Status is required")
    private RaceStatus status;
}