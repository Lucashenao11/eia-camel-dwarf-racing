package com.example.camel_dwarf_racing_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLogResponseDto {

    private Long id;
    private String performedBy;
    private String action;
    private String entityType;
    private Long entityId;
    private LocalDateTime timestamp;
    private String description;
    private String previousValue;
    private String newValue;
}