package com.example.camel_dwarf_racing_api.repository;

import com.example.camel_dwarf_racing_api.model.Race;
import com.example.camel_dwarf_racing_api.model.RaceStatus;
import com.example.camel_dwarf_racing_api.model.RaceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, Long> {

    Page<Race> findByType(RaceType type, Pageable pageable);

    Page<Race> findByStatus(RaceStatus status, Pageable pageable);

    Page<Race> findByTypeAndStatus(RaceType type, RaceStatus status, Pageable pageable);
}