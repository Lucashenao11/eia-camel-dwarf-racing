package com.example.camel_dwarf_racing_api.repository;

import com.example.camel_dwarf_racing_api.model.RaceResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {

    Optional<RaceResult> findByRegistrationId(Long registrationId);

    Page<RaceResult> findByRegistration_Race_Id(Long raceId, Pageable pageable);

    List<RaceResult> findByRegistration_Race_Id(Long raceId);

    boolean existsByRegistration_Race_IdAndFinalPosition(Long raceId, Integer finalPosition);

    boolean existsByRegistration_Race_Id(Long raceId);
}