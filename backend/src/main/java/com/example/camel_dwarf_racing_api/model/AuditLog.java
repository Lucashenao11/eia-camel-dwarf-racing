package com.example.camel_dwarf_racing_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String performedBy; // TODO (Module 1): replace with the authenticated user's username once real auth exists

    @Column(nullable = false)
    private String action; // e.g. "CREATE", "UPDATE", "DELETE", "STATUS_CHANGE"

    @Column(nullable = false)
    private String entityType; // e.g. "Competitor", "Race"

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    private String description;

    @Column(columnDefinition = "TEXT")
    private String previousValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;
}