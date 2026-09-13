package com.example.camel_dwarf_racing_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "races")
@Getter
@Setter
@NoArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    @Column(nullable = false)
    @NotNull(message = "Scheduled date/time is required")
    @Future(message = "A race cannot be scheduled in the past")
    private LocalDateTime scheduledDateTime;

    @Column(nullable = false)
    @NotBlank(message = "Start location is required")
    private String startLocation;

    @Column(nullable = false)
    @NotBlank(message = "Finish location is required")
    private String finishLocation;

    @Column(nullable = false)
    @Positive(message = "Distance must be greater than zero")
    private Double distanceMeters;

    @Column(nullable = false)
    @Positive(message = "Maximum participants must be greater than zero")
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RaceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RaceStatus status = RaceStatus.DRAFT;

    @Column(nullable = false)
    @NotBlank(message = "Organizer is required")
    private String organizer; // TODO (Module 1): once User exists, reference the authenticated organizer instead of a raw String

    @Column(nullable = false)
    @NotNull(message = "Registration deadline is required")
    private LocalDateTime registrationDeadline;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}