package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.RaceRegistrationRequestDto;
import com.example.camel_dwarf_racing_api.exception.DuplicateRegistrationException;
import com.example.camel_dwarf_racing_api.exception.IneligibleParticipantException;
import com.example.camel_dwarf_racing_api.exception.RegistrationClosedException;
import com.example.camel_dwarf_racing_api.model.*;
import com.example.camel_dwarf_racing_api.repository.CompetitorRepository;
import com.example.camel_dwarf_racing_api.repository.RaceRegistrationRepository;
import com.example.camel_dwarf_racing_api.repository.RaceRepository;
import com.example.camel_dwarf_racing_api.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceRegistrationServiceTest {

    @Mock private RaceRegistrationRepository registrationRepository;
    @Mock private RaceRepository raceRepository;
    @Mock private CompetitorRepository competitorRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private AuditLogService auditLogService;

    @InjectMocks
    private RaceRegistrationService registrationService;

    private Race openRace;
    private Competitor activeCompetitor;

    @BeforeEach
    void setUp() {
        openRace = new Race();
        openRace.setId(1L);
        openRace.setStatus(RaceStatus.OPEN_FOR_REGISTRATION);
        openRace.setType(RaceType.INDIVIDUAL);
        openRace.setRegistrationDeadline(LocalDateTime.now().plusDays(5));

        activeCompetitor = new Competitor();
        activeCompetitor.setId(1L);
        activeCompetitor.setNickname("Byte the Camel");
        activeCompetitor.setStatus(CompetitorStatus.ACTIVE);
    }

    // 14. Register an active competitor successfully
    @Test
    void register_activeCompetitor_succeeds() {
        RaceRegistrationRequestDto request = new RaceRegistrationRequestDto();
        request.setCompetitorId(1L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(openRace));
        when(competitorRepository.findById(1L)).thenReturn(Optional.of(activeCompetitor));
        when(registrationRepository.findByRaceIdAndCompetitorId(1L, 1L)).thenReturn(List.of());
        when(registrationRepository.save(any(RaceRegistration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        registrationService.register(1L, request);

        verify(registrationRepository).save(any(RaceRegistration.class));
    }

    // 15. Reject a suspended (non-ACTIVE) competitor
    @Test
    void register_inactiveCompetitor_throwsException() {
        activeCompetitor.setStatus(CompetitorStatus.SUSPENDED);
        RaceRegistrationRequestDto request = new RaceRegistrationRequestDto();
        request.setCompetitorId(1L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(openRace));
        when(competitorRepository.findById(1L)).thenReturn(Optional.of(activeCompetitor));

        assertThrows(IneligibleParticipantException.class,
                () -> registrationService.register(1L, request));

        verify(registrationRepository, never()).save(any());
    }

    // 16. Reject a duplicated registration
    @Test
    void register_duplicateRegistration_throwsException() {
        RaceRegistrationRequestDto request = new RaceRegistrationRequestDto();
        request.setCompetitorId(1L);

        RaceRegistration existing = new RaceRegistration();

        when(raceRepository.findById(1L)).thenReturn(Optional.of(openRace));
        when(competitorRepository.findById(1L)).thenReturn(Optional.of(activeCompetitor));
        when(registrationRepository.findByRaceIdAndCompetitorId(1L, 1L)).thenReturn(List.of(existing));

        assertThrows(DuplicateRegistrationException.class,
                () -> registrationService.register(1L, request));

        verify(registrationRepository, never()).save(any());
    }

    // 17. Reject registration after the race is no longer open
    @Test
    void register_whenRaceNotOpen_throwsException() {
        openRace.setStatus(RaceStatus.CLOSED_FOR_REGISTRATION);
        RaceRegistrationRequestDto request = new RaceRegistrationRequestDto();
        request.setCompetitorId(1L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(openRace));

        assertThrows(RegistrationClosedException.class,
                () -> registrationService.register(1L, request));

        verify(registrationRepository, never()).save(any());
    }
}