package com.example.camel_dwarf_racing_api.repository;

import com.example.camel_dwarf_racing_api.model.RaceRegistration;
import com.example.camel_dwarf_racing_api.model.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceRegistrationRepository extends JpaRepository<RaceRegistration, Long> {

    Page<RaceRegistration> findByRaceId(Long raceId, Pageable pageable);

    List<RaceRegistration> findByRaceIdAndCompetitorId(Long raceId, Long competitorId);

    List<RaceRegistration> findByRaceIdAndTeamId(Long raceId, Long teamId);

    List<RaceRegistration> findByRaceIdAndStatus(Long raceId, RegistrationStatus status);

    long countByRaceIdAndStatus(Long raceId, RegistrationStatus status);
}