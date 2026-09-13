package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.*;
import com.example.camel_dwarf_racing_api.exception.*;
import com.example.camel_dwarf_racing_api.model.*;
import com.example.camel_dwarf_racing_api.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RaceResultService {

    private final RaceResultRepository resultRepository;
    private final RaceRegistrationRepository registrationRepository;

    public RaceResultService(RaceResultRepository resultRepository, RaceRegistrationRepository registrationRepository) {
        this.resultRepository = resultRepository;
        this.registrationRepository = registrationRepository;
    }

    public RaceResultResponseDto recordResult(Long raceId, RaceResultRequestDto requestDto) {

        RaceRegistration registration = registrationRepository.findById(requestDto.getRegistrationId())
                .orElseThrow(() -> new RegistrationNotFoundException(requestDto.getRegistrationId()));

        if (!registration.getRace().getId().equals(raceId)) {
            throw new InvalidResultDataException(
                    "Registration with ID " + requestDto.getRegistrationId() + " does not belong to race " + raceId);
        }

        // Rule: results may only be entered for an IN_PROGRESS race
        if (registration.getRace().getStatus() != RaceStatus.IN_PROGRESS) {
            throw new RaceNotInProgressException(raceId);
        }

        // Rule: only approved participants may receive results
        if (registration.getStatus() != RegistrationStatus.APPROVED) {
            throw new RegistrationNotApprovedException(registration.getId());
        }

        // A registration can only ever have one result
        if (resultRepository.findByRegistrationId(registration.getId()).isPresent()) {
            throw new DuplicateResultException(registration.getId());
        }

        validateResultFields(requestDto);

        // Rule: final positions cannot be duplicated among finishers (this also covers
        // "only one official winner" — position 1 is just one specific final position)
        if (requestDto.getStatus() == ResultStatus.FINISHED
                && resultRepository.existsByRegistration_Race_IdAndFinalPosition(raceId, requestDto.getFinalPosition())) {
            throw new DuplicateFinalPositionException(raceId, requestDto.getFinalPosition());
        }

        RaceResult result = new RaceResult();
        result.setRegistration(registration);
        result.setStartingPosition(registration.getStartingPosition());
        applyRequestDto(result, requestDto);

        RaceResult saved = resultRepository.save(result);
        return toResponseDto(saved);
    }

    public RaceResultResponseDto updateResult(Long id, RaceResultRequestDto requestDto) {
        RaceResult result = resultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id));

        validateResultFields(requestDto);

        Long raceId = result.getRegistration().getRace().getId();

        // Only re-check duplicate position if it's actually changing to a new FINISHED position
        boolean positionChanged = requestDto.getStatus() == ResultStatus.FINISHED
                && !Objects.equals(result.getFinalPosition(), requestDto.getFinalPosition());

        if (positionChanged && resultRepository.existsByRegistration_Race_IdAndFinalPosition(raceId, requestDto.getFinalPosition())) {
            throw new DuplicateFinalPositionException(raceId, requestDto.getFinalPosition());
        }

        applyRequestDto(result, requestDto);

        RaceResult saved = resultRepository.save(result);
        return toResponseDto(saved);
    }

    public Page<RaceResultResponseDto> getResultsForRace(Long raceId, Pageable pageable) {
        return resultRepository.findByRegistration_Race_Id(raceId, pageable)
                .map(this::toResponseDto);
    }

    public RaceResultResponseDto getResultById(Long id) {
        RaceResult result = resultRepository.findById(id)
                .orElseThrow(() -> new ResultNotFoundException(id));
        return toResponseDto(result);
    }

    // ----- Standings -----

    public List<StandingEntryDto> getOverallStandings() {
        return buildStandings(true, true);
    }

    public List<StandingEntryDto> getCompetitorStandings() {
        return buildStandings(true, false);
    }

    public List<StandingEntryDto> getTeamStandings() {
        return buildStandings(false, true);
    }

    private List<StandingEntryDto> buildStandings(boolean includeCompetitors, boolean includeTeams) {
        Map<Long, StandingEntryDto> standingsById = new LinkedHashMap<>();

        for (RaceResult result : resultRepository.findAll()) {
            int points = pointsForPosition(result);
            RaceRegistration registration = result.getRegistration();

            if (includeCompetitors && registration.getCompetitor() != null) {
                Competitor c = registration.getCompetitor();
                StandingEntryDto entry = standingsById.computeIfAbsent(c.getId(), k -> {
                    StandingEntryDto e = new StandingEntryDto();
                    e.setParticipantId(c.getId());
                    e.setParticipantName(c.getNickname());
                    e.setParticipantType("COMPETITOR");
                    e.setPoints(0);
                    return e;
                });
                entry.setPoints(entry.getPoints() + points);
            }

            if (includeTeams && registration.getTeam() != null) {
                Team t = registration.getTeam();
                StandingEntryDto entry = standingsById.computeIfAbsent(t.getId() + 1_000_000, k -> {
                    StandingEntryDto e = new StandingEntryDto();
                    e.setParticipantId(t.getId());
                    e.setParticipantName(t.getName());
                    e.setParticipantType("TEAM");
                    e.setPoints(0);
                    return e;
                });
                entry.setPoints(entry.getPoints() + points);
            }
        }

        List<StandingEntryDto> standings = new ArrayList<>(standingsById.values());
        standings.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));
        return standings;
    }

    private int pointsForPosition(RaceResult result) {
        if (result.getStatus() != ResultStatus.FINISHED || result.getFinalPosition() == null) {
            return 0;
        }
        return switch (result.getFinalPosition()) {
            case 1 -> 10;
            case 2 -> 7;
            case 3 -> 5;
            case 4 -> 3;
            case 5 -> 1;
            default -> 0;
        };
    }

    // ----- helpers -----

    private void validateResultFields(RaceResultRequestDto requestDto) {
        if (requestDto.getStatus() == ResultStatus.FINISHED) {
            if (requestDto.getFinalPosition() == null || requestDto.getFinalPosition() <= 0) {
                throw new InvalidResultDataException("A FINISHED result requires a positive final position.");
            }
            if (requestDto.getCompletionTimeSeconds() == null || requestDto.getCompletionTimeSeconds() <= 0) {
                throw new InvalidResultDataException("A FINISHED result requires a positive completion time.");
            }
        } else {
            // Rule: a disqualified participant cannot win — enforced generally: non-finishers hold no final position
            if (requestDto.getFinalPosition() != null) {
                throw new InvalidResultDataException("Only a FINISHED result may have a final position.");
            }
        }
    }

    private void applyRequestDto(RaceResult result, RaceResultRequestDto requestDto) {
        result.setFinalPosition(requestDto.getFinalPosition());
        result.setCompletionTimeSeconds(requestDto.getCompletionTimeSeconds());
        result.setPenaltyTimeSeconds(requestDto.getPenaltyTimeSeconds() != null ? requestDto.getPenaltyTimeSeconds() : 0.0);
        result.setStatus(requestDto.getStatus());
        result.setNotes(requestDto.getNotes());
    }

    private RaceResultResponseDto toResponseDto(RaceResult result) {
        RaceResultResponseDto dto = new RaceResultResponseDto();
        dto.setId(result.getId());
        dto.setRegistrationId(result.getRegistration().getId());

        RaceRegistration reg = result.getRegistration();
        dto.setParticipantName(reg.getCompetitor() != null
                ? reg.getCompetitor().getNickname()
                : reg.getTeam().getName());

        dto.setStartingPosition(result.getStartingPosition());
        dto.setFinalPosition(result.getFinalPosition());
        dto.setCompletionTimeSeconds(result.getCompletionTimeSeconds());
        dto.setPenaltyTimeSeconds(result.getPenaltyTimeSeconds());
        dto.setStatus(result.getStatus());
        dto.setNotes(result.getNotes());
        dto.setRecordedAt(result.getRecordedAt());
        return dto;
    }
}