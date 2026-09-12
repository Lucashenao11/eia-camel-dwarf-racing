package com.example.camel_dwarf_racing_api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column()
    private String description;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "The name of the coach cannot be empty")
    private String coach;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamStatus status = TeamStatus.ACTIVE;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Competitor> competitors = new ArrayList<>();
}
