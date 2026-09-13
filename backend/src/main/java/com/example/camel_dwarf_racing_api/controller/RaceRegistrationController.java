package com.example.camel_dwarf_racing_api.controller;

import com.example.camel_dwarf_racing_api.dto.*;
import com.example.camel_dwarf_racing_api.service.RaceRegistrationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RaceRegistrationController {

    private final RaceRegistrationService registrationService;

    public RaceRegistrationController(RaceRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/api/races/{raceId}/registrations")
    public ResponseEntity<RaceRegistrationResponseDto> register(
            @PathVariable Long raceId,
            @Valid @RequestBody RaceRegistrationRequestDto requestDto) {
        RaceRegistrationResponseDto created = registrationService.register(raceId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/api/races/{raceId}/registrations")
    public ResponseEntity<Page<RaceRegistrationResponseDto>> getRegistrationsForRace(
            @PathVariable Long raceId, Pageable pageable) {
        return ResponseEntity.ok(registrationService.getRegistrationsForRace(raceId, pageable));
    }

    @GetMapping("/api/registrations/{id}")
    public ResponseEntity<RaceRegistrationResponseDto> getRegistrationById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationById(id));
    }

    @PatchMapping("/api/registrations/{id}/approve")
    public ResponseEntity<RaceRegistrationResponseDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.approve(id));
    }

    @PatchMapping("/api/registrations/{id}/reject")
    public ResponseEntity<RaceRegistrationResponseDto> reject(
            @PathVariable Long id,
            @Valid @RequestBody RegistrationRejectionDto rejectionDto) {
        return ResponseEntity.ok(registrationService.reject(id, rejectionDto));
    }

    @DeleteMapping("/api/registrations/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        registrationService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}