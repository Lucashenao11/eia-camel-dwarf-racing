package com.example.camel_dwarf_racing_api.controller;

import com.example.camel_dwarf_racing_api.dto.AuditLogResponseDto;
import com.example.camel_dwarf_racing_api.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // TODO (Module 1): restrict this endpoint to ADMIN role only, once real auth exists.
    // Spec: "Only administrators may view the complete audit log."
    @GetMapping
    public ResponseEntity<Page<AuditLogResponseDto>> getAllLogs(
            @RequestParam(required = false) String entityType,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAllLogs(entityType, pageable));
    }
}