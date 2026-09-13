package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.RaceRequestDto;
import com.example.camel_dwarf_racing_api.dto.RaceResponseDto;
import com.example.camel_dwarf_racing_api.dto.RaceStatusUpdateDto;
import com.example.camel_dwarf_racing_api.exception.*;
import com.example.camel_dwarf_racing_api.model.Race;
import com.example.camel_dwarf_racing_api.model.RaceStatus;
import com.example.camel_dwarf_racing_api.model.RaceType;
import com.example.camel_dwarf_racing_api.model.RegistrationStatus;
import com.example.camel_dwarf_racing_api.repository.RaceRegistrationRepository;
import com.example.camel_dwarf_racing_api.repository.RaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final RaceRegistrationRepository raceRegistrationRepository;
    private final AuditLogService auditLogService;
    // Legal status transitions: from -> allowed next states.
    // COMPLETED and CANCELLED have no outgoing transitions (terminal states).
    private static final Map<RaceStatus, Set<RaceStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(RaceStatus.class);
    static {
        ALLOWED_TRANSITIONS.put(RaceStatus.DRAFT, EnumSet.of(RaceStatus.OPEN_FOR_REGISTRATION, RaceStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(RaceStatus.OPEN_FOR_REGISTRATION,
                EnumSet.of(RaceStatus.CLOSED_FOR_REGISTRATION, RaceStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(RaceStatus.CLOSED_FOR_REGISTRATION,
                EnumSet.of(RaceStatus.IN_PROGRESS, RaceStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(RaceStatus.IN_PROGRESS, EnumSet.of(RaceStatus.COMPLETED));
        ALLOWED_TRANSITIONS.put(RaceStatus.COMPLETED, EnumSet.noneOf(RaceStatus.class));
        ALLOWED_TRANSITIONS.put(RaceStatus.CANCELLED, EnumSet.noneOf(RaceStatus.class));
    }

    public RaceService(RaceRepository raceRepository, RaceRegistrationRepository raceRegistrationRepository,
            AuditLogService auditLogService) {
        this.raceRepository = raceRepository;
        this.raceRegistrationRepository = raceRegistrationRepository;
        this.auditLogService = auditLogService;
    }

    public RaceResponseDto createRace(RaceRequestDto requestDto) {
        validateDates(requestDto.getRegistrationDeadline(), requestDto.getScheduledDateTime());

        Race race = new Race();
        applyRequestDto(race, requestDto);

        Race saved = raceRepository.save(race);
        auditLogService.record("system", "CREATE", "Race", saved.getId(),
                "Created race: " + saved.getName());
        return toResponseDto(saved);
    }

    public Page<RaceResponseDto> getAllRaces(RaceType type, RaceStatus status, Pageable pageable) {
        Page<Race> races;

        if (type != null && status != null) {
            races = raceRepository.findByTypeAndStatus(type, status, pageable);
        } else if (type != null) {
            races = raceRepository.findByType(type, pageable);
        } else if (status != null) {
            races = raceRepository.findByStatus(status, pageable);
        } else {
            races = raceRepository.findAll(pageable);
        }

        return races.map(this::toResponseDto);
    }

    public RaceResponseDto getRaceById(Long id) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException(id));
        return toResponseDto(race);
    }

    public RaceResponseDto updateRace(Long id, RaceRequestDto requestDto) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException(id));

        if (race.getStatus() == RaceStatus.COMPLETED) {
            throw new RaceAlreadyCompletedException(id);
        }

        validateDates(requestDto.getRegistrationDeadline(), requestDto.getScheduledDateTime());

        applyRequestDto(race, requestDto);
        race.setUpdatedAt(LocalDateTime.now());

        Race saved = raceRepository.save(race);
        return toResponseDto(saved);
    }

    public RaceResponseDto updateRaceStatus(Long id, RaceStatusUpdateDto requestDto) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException(id));

        RaceStatus currentStatus = race.getStatus();
        RaceStatus newStatus = requestDto.getStatus();

        if (!ALLOWED_TRANSITIONS.get(currentStatus).contains(newStatus)) {
            throw new InvalidRaceStatusTransitionException(currentStatus, newStatus);
        }

        if (newStatus == RaceStatus.IN_PROGRESS) {
            long approvedCount = raceRegistrationRepository.countByRaceIdAndStatus(id, RegistrationStatus.APPROVED);
            if (approvedCount < 2) {
                throw new InsufficientParticipantsException(id, approvedCount);
            }
        }
        // TODO (Module 6): before allowing COMPLETED, verify official results have been
        // recorded
        // ("a race cannot be completed without official results").
        String previousStatus = currentStatus.toString();
        race.setStatus(newStatus);
        race.setUpdatedAt(LocalDateTime.now());

        Race saved = raceRepository.save(race);
        String action = (newStatus == RaceStatus.CANCELLED) ? "CANCEL" : "STATUS_CHANGE";
        auditLogService.record("system", action, "Race", saved.getId(),
                "Race status changed", previousStatus, newStatus.toString());
        return toResponseDto(saved);
    }

    public void deleteRace(Long id) {
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new RaceNotFoundException(id));

        // TODO (Module 5): once RaceRegistration exists, consider blocking hard-delete
        // for races that already have registrations, similar to Competitor/Team guards.
        auditLogService.record("system", "DELETE", "Race", race.getId(),
                "Deleted race: " + race.getName());
        raceRepository.delete(race);
    }

    private void validateDates(LocalDateTime registrationDeadline, LocalDateTime scheduledDateTime) {
        if (!registrationDeadline.isBefore(scheduledDateTime)) {
            throw new InvalidRaceDatesException(
                    "The registration deadline must be earlier than the race's scheduled start time.");
        }
    }

    private void applyRequestDto(Race race, RaceRequestDto requestDto) {
        race.setName(requestDto.getName());
        race.setDescription(requestDto.getDescription());
        race.setScheduledDateTime(requestDto.getScheduledDateTime());
        race.setStartLocation(requestDto.getStartLocation());
        race.setFinishLocation(requestDto.getFinishLocation());
        race.setDistanceMeters(requestDto.getDistanceMeters());
        race.setMaxParticipants(requestDto.getMaxParticipants());
        race.setType(requestDto.getType());
        race.setOrganizer(requestDto.getOrganizer());
        race.setRegistrationDeadline(requestDto.getRegistrationDeadline());
    }

    private RaceResponseDto toResponseDto(Race race) {
        RaceResponseDto dto = new RaceResponseDto();
        dto.setId(race.getId());
        dto.setName(race.getName());
        dto.setDescription(race.getDescription());
        dto.setScheduledDateTime(race.getScheduledDateTime());
        dto.setStartLocation(race.getStartLocation());
        dto.setFinishLocation(race.getFinishLocation());
        dto.setDistanceMeters(race.getDistanceMeters());
        dto.setMaxParticipants(race.getMaxParticipants());
        dto.setType(race.getType());
        dto.setStatus(race.getStatus());
        dto.setOrganizer(race.getOrganizer());
        dto.setRegistrationDeadline(race.getRegistrationDeadline());
        dto.setCreatedAt(race.getCreatedAt());
        dto.setUpdatedAt(race.getUpdatedAt());
        return dto;
    }
}