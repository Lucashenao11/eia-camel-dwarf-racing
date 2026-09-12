package com.example.camel_dwarf_racing_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.camel_dwarf_racing_api.model.Team;
import com.example.camel_dwarf_racing_api.model.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

    boolean existsByName(String name);

    boolean existsByCoach(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByCoachAndIdNot(String coach, Long id);

    Page<Team> findByStatus(TeamStatus status, Pageable pageable);
}
