package com.example.camel_dwarf_racing_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.example.camel_dwarf_racing_api.dto.TeamRequestDto;
import com.example.camel_dwarf_racing_api.dto.TeamResponseDto;
import com.example.camel_dwarf_racing_api.model.TeamStatus;
import com.example.camel_dwarf_racing_api.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(
            @Valid @RequestBody TeamRequestDto requestDto) {
        TeamResponseDto created = teamService.createTeam(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{teamId}/members/{competitorId}")
    public ResponseEntity<TeamResponseDto> addMemberToTeam(
            @PathVariable Long teamId,
            @PathVariable Long competitorId) {

        TeamResponseDto updated = teamService.addMemberToTeam(teamId, competitorId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<Page<TeamResponseDto>> getAllTeams(
            @RequestParam(required = false) TeamStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(teamService.getAllTeams(status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequestDto requestDto) {
        return ResponseEntity.ok(teamService.updateTeam(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}/members/{competitorId}")
    public ResponseEntity<TeamResponseDto> removeMemberFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long competitorId) {

        TeamResponseDto updated = teamService.removeMemberFromTeam(teamId, competitorId);
        return ResponseEntity.ok(updated);
    }
}
