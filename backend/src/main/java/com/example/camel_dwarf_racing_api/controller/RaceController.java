package com.example.camel_dwarf_racing_api.controller;

import com.example.camel_dwarf_racing_api.dto.RaceRequestDto;
import com.example.camel_dwarf_racing_api.dto.RaceResponseDto;
import com.example.camel_dwarf_racing_api.dto.RaceStatusUpdateDto;
import com.example.camel_dwarf_racing_api.model.RaceStatus;
import com.example.camel_dwarf_racing_api.model.RaceType;
import com.example.camel_dwarf_racing_api.service.RaceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/races")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @PostMapping
    public ResponseEntity<RaceResponseDto> createRace(@Valid @RequestBody RaceRequestDto requestDto) {
        RaceResponseDto created = raceService.createRace(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<RaceResponseDto>> getAllRaces(
            @RequestParam(required = false) RaceType type,
            @RequestParam(required = false) RaceStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(raceService.getAllRaces(type, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponseDto> getRaceById(@PathVariable Long id) {
        return ResponseEntity.ok(raceService.getRaceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceResponseDto> updateRace(
            @PathVariable Long id,
            @Valid @RequestBody RaceRequestDto requestDto) {
        return ResponseEntity.ok(raceService.updateRace(id, requestDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RaceResponseDto> updateRaceStatus(
            @PathVariable Long id,
            @Valid @RequestBody RaceStatusUpdateDto requestDto) {
        return ResponseEntity.ok(raceService.updateRaceStatus(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }
}