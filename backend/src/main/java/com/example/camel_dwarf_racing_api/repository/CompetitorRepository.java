package com.example.camel_dwarf_racing_api.repository;

import com.example.camel_dwarf_racing_api.model.Competitor;
import com.example.camel_dwarf_racing_api.model.CompetitorStatus;
import com.example.camel_dwarf_racing_api.model.CompetitorType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {

    Optional<Competitor> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
    boolean existsByNicknameAndIdNot(String nickname, Long id);

    Page<Competitor> findByTypeAndStatus(CompetitorType type, CompetitorStatus status, Pageable pageable);
    Page<Competitor> findByType(CompetitorType type, Pageable pageable);
    Page<Competitor> findByStatus(CompetitorStatus status, Pageable pageable);
    

}