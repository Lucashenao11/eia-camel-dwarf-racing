package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.*;
import com.example.camel_dwarf_racing_api.exception.*;
import com.example.camel_dwarf_racing_api.model.*;
import com.example.camel_dwarf_racing_api.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RaceRegistrationService {

    private final RaceRegistrationRepository registrationRepository;
    private final RaceRepository raceRepository;
    private final CompetitorRepository competitorRepository;
    private final TeamRepository teamRepository;
    private final AuditLogService auditLogService;

    public RaceRegistrationService(
            RaceRegistrationRepository registrationRepository,
            RaceRepository raceRepository,
            CompetitorRepository competitorRepository,
            TeamRepository teamRepository,
            AuditLogService auditLogService) {
        this.registrationRepository = registrationRepository;
        this.raceRepository = raceRepository;
        this.competitorRepository = competitorRepository;
        this.teamRepository = teamRepository;
        this.auditLogService = auditLogService;
    }

    public RaceRegistrationResponseDto register(Long raceId, RaceRegistrationRequestDto requestDto) {

        Race race = raceRepository.findById(raceId)
                .orElseThrow(() -> new RaceNotFoundException(raceId));

        // Rule: registration allowed only while OPEN_FOR_REGISTRATION and before the
        // deadline
        if (race.getStatus() != RaceStatus.OPEN_FOR_REGISTRATION) {
            throw new RegistrationClosedException(raceId);
        }
        if (LocalDateTime.now().isAfter(race.getRegistrationDeadline())) {
            throw new RegistrationClosedException(raceId);
        }

        boolean hasCompetitor = requestDto.getCompetitorId() != null;
        boolean hasTeam = requestDto.getTeamId() != null;

        // Rule: exactly one of competitor/team, never both, never neither
        if (hasCompetitor == hasTeam) {
            throw new InvalidRegistrationRequestException(
                    "Exactly one of competitorId or teamId must be provided.");
        }

        // Rule: registration type must match race type
        if (race.getType() == RaceType.INDIVIDUAL && hasTeam) {
            throw new InvalidRegistrationRequestException(
                    "This race is INDIVIDUAL only; teams cannot register.");
        }
        if (race.getType() == RaceType.TEAM && hasCompetitor) {
            throw new InvalidRegistrationRequestException(
                    "This race is TEAM only; individual competitors cannot register.");
        }

        RaceRegistration registration = new RaceRegistration();
        registration.setRace(race);

        if (hasCompetitor) {
            registerCompetitor(registration, race, requestDto.getCompetitorId());
        } else {
            registerTeam(registration, race, requestDto.getTeamId());
        }

        RaceRegistration saved = registrationRepository.save(registration);
        auditLogService.record("system", "CREATE", "RaceRegistration", saved.getId(),
                "New registration for race " + raceId);
        return toResponseDto(saved);
    }

    private void registerCompetitor(RaceRegistration registration, Race race, Long competitorId) {
        Competitor competitor = competitorRepository.findById(competitorId)
                .orElseThrow(() -> new CompetitorNotFoundException(competitorId));

        // Rule: all individual competitors must be eligible (ACTIVE)
        if (competitor.getStatus() != CompetitorStatus.ACTIVE) {
            throw new IneligibleParticipantException(
                    "Competitor with ID " + competitorId + " is not ACTIVE and cannot register.");
        }

        // Rule: a competitor cannot be registered twice in the same race
        boolean alreadyRegistered = !registrationRepository
                .findByRaceIdAndCompetitorId(race.getId(), competitorId).isEmpty();
        if (alreadyRegistered) {
            throw new DuplicateRegistrationException(
                    "Competitor with ID " + competitorId + " is already registered for this race.");
        }

        // Rule: cannot compete simultaneously as an individual AND as a team member in
        // the same race
        if (competitor.getTeam() != null) {
            boolean teamAlreadyRegistered = !registrationRepository
                    .findByRaceIdAndTeamId(race.getId(), competitor.getTeam().getId()).isEmpty();
            if (teamAlreadyRegistered) {
                throw new DuplicateRegistrationException(
                        "Competitor with ID " + competitorId +
                                " belongs to a team already registered for this race.");
            }
        }

        registration.setCompetitor(competitor);
    }

    private void registerTeam(RaceRegistration registration, Race race, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        // Rule: a suspended team cannot enter a race
        if (team.getStatus() != TeamStatus.ACTIVE) {
            throw new IneligibleParticipantException(
                    "Team with ID " + teamId + " is not ACTIVE and cannot register.");
        }

        // Rule: a team must contain at least one competitor before entering a race
        if (team.getCompetitors().isEmpty()) {
            throw new IneligibleParticipantException(
                    "Team with ID " + teamId + " has no members and cannot register.");
        }

        // Rule: a team cannot be registered twice in the same race
        boolean alreadyRegistered = !registrationRepository
                .findByRaceIdAndTeamId(race.getId(), teamId).isEmpty();
        if (alreadyRegistered) {
            throw new DuplicateRegistrationException(
                    "Team with ID " + teamId + " is already registered for this race.");
        }

        // Rule: none of this team's members may already be individually registered for
        // the same race
        for (Competitor member : team.getCompetitors()) {
            boolean memberAlreadyRegistered = !registrationRepository
                    .findByRaceIdAndCompetitorId(race.getId(), member.getId()).isEmpty();
            if (memberAlreadyRegistered) {
                throw new DuplicateRegistrationException(
                        "Team member with ID " + member.getId() +
                                " is already individually registered for this race.");
            }
        }

        registration.setTeam(team);
    }

    public Page<RaceRegistrationResponseDto> getRegistrationsForRace(Long raceId, Pageable pageable) {
        return registrationRepository.findByRaceId(raceId, pageable)
                .map(this::toResponseDto);
    }

    public RaceRegistrationResponseDto getRegistrationById(Long id) {
        RaceRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));
        return toResponseDto(registration);
    }

    public RaceRegistrationResponseDto approve(Long id) {
        RaceRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));

        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new RegistrationAlreadyProcessedException(id);
        }

        registration.setStatus(RegistrationStatus.APPROVED);
        RaceRegistration saved = registrationRepository.save(registration);
        auditLogService.record("system", "APPROVE", "RaceRegistration", saved.getId(),
                "Registration approved");
        return toResponseDto(saved);
    }

    public RaceRegistrationResponseDto reject(Long id, RegistrationRejectionDto rejectionDto) {
        RaceRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));

        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new RegistrationAlreadyProcessedException(id);
        }

        registration.setStatus(RegistrationStatus.REJECTED);
        registration.setValidationNotes(rejectionDto.getReason());
        RaceRegistration saved = registrationRepository.save(registration);
        auditLogService.record("system", "REJECT", "RaceRegistration", saved.getId(),
                "Registration rejected: " + rejectionDto.getReason());
        return toResponseDto(saved);
    }

    public void cancel(Long id) {
        RaceRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }

    private RaceRegistrationResponseDto toResponseDto(RaceRegistration registration) {
        RaceRegistrationResponseDto dto = new RaceRegistrationResponseDto();
        dto.setId(registration.getId());
        dto.setRaceId(registration.getRace().getId());
        dto.setRaceName(registration.getRace().getName());

        if (registration.getCompetitor() != null) {
            dto.setCompetitorId(registration.getCompetitor().getId());
            dto.setCompetitorNickname(registration.getCompetitor().getNickname());
        }
        if (registration.getTeam() != null) {
            dto.setTeamId(registration.getTeam().getId());
            dto.setTeamName(registration.getTeam().getName());
        }

        dto.setRegistrationDate(registration.getRegistrationDate());
        dto.setStatus(registration.getStatus());
        dto.setStartingPosition(registration.getStartingPosition());
        dto.setValidationNotes(registration.getValidationNotes());
        return dto;
    }
}