package com.example.camel_dwarf_racing_api.dto;

import com.example.camel_dwarf_racing_api.model.CompetitorStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitorStatusUpdateDto {

    @NotNull(message = "Status is required")
    private CompetitorStatus status;
}