package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.AuditLogResponseDto;
import com.example.camel_dwarf_racing_api.model.AuditLog;
import com.example.camel_dwarf_racing_api.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void record(String performedBy, String action, String entityType, Long entityId, String description) {
        record(performedBy, action, entityType, entityId, description, null, null);
    }

    public void record(String performedBy, String action, String entityType, Long entityId,
                        String description, String previousValue, String newValue) {
        AuditLog log = new AuditLog();
        log.setPerformedBy(performedBy);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        log.setPreviousValue(previousValue);
        log.setNewValue(newValue);
        auditLogRepository.save(log);
    }

    public Page<AuditLogResponseDto> getAllLogs(String entityType, Pageable pageable) {
        Page<AuditLog> logs = (entityType != null)
                ? auditLogRepository.findByEntityType(entityType, pageable)
                : auditLogRepository.findAll(pageable);
        return logs.map(this::toResponseDto);
    }

    private AuditLogResponseDto toResponseDto(AuditLog log) {
        AuditLogResponseDto dto = new AuditLogResponseDto();
        dto.setId(log.getId());
        dto.setPerformedBy(log.getPerformedBy());
        dto.setAction(log.getAction());
        dto.setEntityType(log.getEntityType());
        dto.setEntityId(log.getEntityId());
        dto.setTimestamp(log.getTimestamp());
        dto.setDescription(log.getDescription());
        dto.setPreviousValue(log.getPreviousValue());
        dto.setNewValue(log.getNewValue());
        return dto;
    }
}