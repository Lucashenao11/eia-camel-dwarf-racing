package com.example.camel_dwarf_racing_api.controller;

import com.example.camel_dwarf_racing_api.dto.CompetitorRequestDto;
import com.example.camel_dwarf_racing_api.dto.CompetitorResponseDto;
import com.example.camel_dwarf_racing_api.dto.CompetitorStatusUpdateDto;
import com.example.camel_dwarf_racing_api.model.CompetitorStatus;
import com.example.camel_dwarf_racing_api.model.CompetitorType;
import com.example.camel_dwarf_racing_api.service.CompetitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/competitors")
public class CompetitorController {

    private final CompetitorService competitorService;

    public CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @PostMapping
    public ResponseEntity<CompetitorResponseDto> createCompetitor(
            @Valid @RequestBody CompetitorRequestDto requestDto) {

        CompetitorResponseDto created = competitorService.createCompetitor(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<CompetitorResponseDto>> getAllCompetitors(
            @RequestParam(required = false) CompetitorType type,
            @RequestParam(required = false) CompetitorStatus status,
            Pageable pageable) {

        Page<CompetitorResponseDto> fetched = competitorService.getAllCompetitors(type, status, pageable);
        return ResponseEntity.ok(fetched);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitorResponseDto> getCompetitorById(@PathVariable Long id) {
        CompetitorResponseDto competitor = competitorService.getCompetitorById(id);
        return ResponseEntity.ok(competitor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitorResponseDto> updateCompetitor(
            @PathVariable Long id,
            @Valid @RequestBody CompetitorRequestDto requestDto) {

        CompetitorResponseDto updated = competitorService.updateCompetitor(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CompetitorResponseDto> updateCompetitorStatus(
        @PathVariable Long id,
        @Valid @RequestBody CompetitorStatusUpdateDto requestDto) {

        CompetitorResponseDto updated = competitorService.updateCompetitorStatus(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetitor(@PathVariable Long id){

        competitorService.deleteCompetitor(id);
        return ResponseEntity.noContent().build();
    }
}