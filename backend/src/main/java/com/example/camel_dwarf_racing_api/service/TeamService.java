package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.CompetitorResponseDto;
import com.example.camel_dwarf_racing_api.dto.TeamRequestDto;
import com.example.camel_dwarf_racing_api.dto.TeamResponseDto;
import com.example.camel_dwarf_racing_api.exception.*;
import com.example.camel_dwarf_racing_api.model.Competitor;
import com.example.camel_dwarf_racing_api.model.Team;
import com.example.camel_dwarf_racing_api.model.TeamStatus;
import com.example.camel_dwarf_racing_api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class TeamService {

    @Value("${team.max-members}")
    private int maxTeamMembers;
    private final TeamRepository teamRepository;
    private final CompetitorRepository competitorRepository;
    private final AuditLogService auditLogService;

    public TeamService(TeamRepository teamRepository, CompetitorRepository competitorRepository,
            AuditLogService auditLogService) {
        this.teamRepository = teamRepository;
        this.competitorRepository = competitorRepository;
        this.auditLogService = auditLogService;
    }

    public TeamResponseDto createTeam(TeamRequestDto requestDto) {
        if (teamRepository.existsByName(requestDto.getName())) {
            throw new DuplicateTeamNameException(requestDto.getName());
        }
        if (teamRepository.existsByCoach(requestDto.getCoach())) {
            throw new DuplicateCoachException(requestDto.getCoach());
        }

        Team team = new Team();
        team.setName(requestDto.getName());
        team.setDescription(requestDto.getDescription());
        team.setCoach(requestDto.getCoach());

        Team saved = teamRepository.save(team);
        auditLogService.record("system", "CREATE", "Team", saved.getId(),
                "Created team: " + saved.getName());
        return toResponseDto(saved);
    }

    private TeamResponseDto toResponseDto(Team team) {
        TeamResponseDto dto = new TeamResponseDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        dto.setCoach(team.getCoach());
        dto.setCreationDate(team.getCreationDate());
        dto.setStatus(team.getStatus());
        dto.setCompetitors(
                team.getCompetitors()
                        .stream()
                        .map(this::toCompetitorResponseDto)
                        .toList());
        return dto;
    }

    private CompetitorResponseDto toCompetitorResponseDto(Competitor competitor) {
        CompetitorResponseDto dto = new CompetitorResponseDto();
        dto.setId(competitor.getId());
        dto.setName(competitor.getName());
        dto.setNickname(competitor.getNickname());
        dto.setType(competitor.getType());
        dto.setDateOfBirth(competitor.getDateOfBirth());
        dto.setWeight(competitor.getWeight());
        dto.setHeight(competitor.getHeight());
        dto.setCountryOfOrigin(competitor.getCountryOfOrigin());
        dto.setStatus(competitor.getStatus());
        dto.setRegistrationDate(competitor.getRegistrationDate());
        return dto;
    }

    public TeamResponseDto addMemberToTeam(Long teamId, Long competitorId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        Competitor competitor = competitorRepository.findById(competitorId)
                .orElseThrow(() -> new CompetitorNotFoundException(competitorId));

        if (competitor.getTeam() != null && !competitor.getTeam().getId().equals(teamId)) {
            throw new CompetitorAlreadyOnTeamException(competitorId);
        }

        if (team.getCompetitors().contains(competitor)) {
            throw new DuplicateTeamMembershipException(teamId, competitorId);
        }

        if (team.getCompetitors().size() >= maxTeamMembers) {
            throw new TeamCapacityExceededException(teamId, maxTeamMembers);
        }

        competitor.setTeam(team);
        competitorRepository.save(competitor);

        team.getCompetitors().add(competitor);
        Team saved = teamRepository.save(team);
        auditLogService.record("system", "UPDATE", "Team", saved.getId(),
                "Added competitor " + competitor.getNickname() + " to team " + saved.getName());
        return toResponseDto(saved);
    }

    public Page<TeamResponseDto> getAllTeams(TeamStatus status, Pageable pageable) {
        Page<Team> teams = (status != null)
                ? teamRepository.findByStatus(status, pageable)
                : teamRepository.findAll(pageable);
        return teams.map(this::toResponseDto);
    }

    public TeamResponseDto getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        return toResponseDto(team);
    }

    public TeamResponseDto updateTeam(Long id, TeamRequestDto requestDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        if (teamRepository.existsByNameAndIdNot(requestDto.getName(), id)) {
            throw new DuplicateTeamNameException(requestDto.getName());
        }
        if (teamRepository.existsByCoachAndIdNot(requestDto.getCoach(), id)) {
            throw new DuplicateCoachException(requestDto.getCoach());
        }

        team.setName(requestDto.getName());
        team.setDescription(requestDto.getDescription());
        team.setCoach(requestDto.getCoach());

        Team saved = teamRepository.save(team);
        return toResponseDto(saved);
    }

    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        // TODO (Module 4+): once Race/RaceResult exist, check for official race history
        // and set status = DEACTIVATED instead of deleting, per the spec's rule that a
        // team with official race history cannot be deleted.
        auditLogService.record("system", "DELETE", "Team", team.getId(),
                "Deleted team: " + team.getName());
        teamRepository.delete(team);
    }

    public TeamResponseDto removeMemberFromTeam(Long teamId, Long competitorId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        Competitor competitor = competitorRepository.findById(competitorId)
                .orElseThrow(() -> new CompetitorNotFoundException(competitorId));

        if (competitor.getTeam() == null || !competitor.getTeam().getId().equals(teamId)) {
            throw new CompetitorNotOnTeamException(competitorId, teamId);
        }

        competitor.setTeam(null);
        competitorRepository.save(competitor);

        team.getCompetitors().remove(competitor);
        Team saved = teamRepository.save(team);
        auditLogService.record("system", "UPDATE", "Team", saved.getId(),
                "Removed competitor " + competitor.getNickname() + " from team " + saved.getName());
        return toResponseDto(saved);
    }
}