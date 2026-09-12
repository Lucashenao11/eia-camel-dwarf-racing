package com.example.camel_dwarf_racing_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "competitors")
@Getter
@Setter
@NoArgsConstructor
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Nickname cannot be empty")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetitorType type;

    @Column(nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Positive(message = "Weight must be positive")
    private Double weight;

    @Column(nullable = false)
    @Positive(message = "Height must be positive")
    private Double height;

    @Column(nullable = false)
    @NotBlank(message = "Country of origin cannot be empty")
    private String countryOfOrigin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetitorStatus status = CompetitorStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDate registrationDate = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}