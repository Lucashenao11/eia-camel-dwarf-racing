package com.example.camel_dwarf_racing_api.controller;

import com.example.camel_dwarf_racing_api.dto.*;
import com.example.camel_dwarf_racing_api.service.RaceResultService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RaceResultController {

    private final RaceResultService resultService;

    public RaceResultController(RaceResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping("/api/races/{raceId}/results")
    public ResponseEntity<RaceResultResponseDto> recordResult(
            @PathVariable Long raceId,
            @Valid @RequestBody RaceResultRequestDto requestDto) {
        RaceResultResponseDto created = resultService.recordResult(raceId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/api/races/{raceId}/results")
    public ResponseEntity<Page<RaceResultResponseDto>> getResultsForRace(
            @PathVariable Long raceId, Pageable pageable) {
        return ResponseEntity.ok(resultService.getResultsForRace(raceId, pageable));
    }

    @PutMapping("/api/results/{id}")
    public ResponseEntity<RaceResultResponseDto> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody RaceResultRequestDto requestDto) {
        return ResponseEntity.ok(resultService.updateResult(id, requestDto));
    }

    @GetMapping("/api/results/{id}")
    public ResponseEntity<RaceResultResponseDto> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    @GetMapping("/api/standings")
    public ResponseEntity<List<StandingEntryDto>> getOverallStandings() {
        return ResponseEntity.ok(resultService.getOverallStandings());
    }

    @GetMapping("/api/standings/competitors")
    public ResponseEntity<List<StandingEntryDto>> getCompetitorStandings() {
        return ResponseEntity.ok(resultService.getCompetitorStandings());
    }

    @GetMapping("/api/standings/teams")
    public ResponseEntity<List<StandingEntryDto>> getTeamStandings() {
        return ResponseEntity.ok(resultService.getTeamStandings());
    }
}