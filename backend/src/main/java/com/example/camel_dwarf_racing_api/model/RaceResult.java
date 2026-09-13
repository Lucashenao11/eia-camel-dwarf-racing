package com.example.camel_dwarf_racing_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "race_results")
@Getter
@Setter
@NoArgsConstructor
public class RaceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private RaceRegistration registration;

    private Integer startingPosition;

    private Integer finalPosition; // null unless status == FINISHED

    private Double completionTimeSeconds; // null unless status == FINISHED

    private Double penaltyTimeSeconds = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultStatus status;

    private String notes;

    // TODO (Module 1): replace with the authenticated user once real auth exists
    private String recordedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();
}