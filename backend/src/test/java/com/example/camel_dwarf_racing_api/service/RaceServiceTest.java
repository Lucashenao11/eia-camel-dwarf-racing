package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.RaceRequestDto;
import com.example.camel_dwarf_racing_api.dto.RaceStatusUpdateDto;
import com.example.camel_dwarf_racing_api.exception.InvalidRaceDatesException;
import com.example.camel_dwarf_racing_api.exception.InvalidRaceStatusTransitionException;
import com.example.camel_dwarf_racing_api.model.Race;
import com.example.camel_dwarf_racing_api.model.RaceStatus;
import com.example.camel_dwarf_racing_api.model.RaceType;
import com.example.camel_dwarf_racing_api.model.RegistrationStatus;
import com.example.camel_dwarf_racing_api.repository.RaceRegistrationRepository;
import com.example.camel_dwarf_racing_api.repository.RaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private RaceRegistrationRepository raceRegistrationRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private RaceService raceService;

    private RaceRequestDto validRequest;
    private Race draftRace;

    @BeforeEach
    void setUp() {
        validRequest = new RaceRequestDto();
        validRequest.setName("Mountain Sprint");
        validRequest.setScheduledDateTime(LocalDateTime.now().plusDays(30));
        validRequest.setStartLocation("Village");
        validRequest.setFinishLocation("Peak");
        validRequest.setDistanceMeters(5000.0);
        validRequest.setMaxParticipants(20);
        validRequest.setType(RaceType.INDIVIDUAL);
        validRequest.setOrganizer("Race Committee");
        validRequest.setRegistrationDeadline(LocalDateTime.now().plusDays(25));

        draftRace = new Race();
        draftRace.setId(1L);
        draftRace.setName("Mountain Sprint");
        draftRace.setStatus(RaceStatus.DRAFT);
        draftRace.setScheduledDateTime(LocalDateTime.now().plusDays(30));
        draftRace.setRegistrationDeadline(LocalDateTime.now().plusDays(25));
    }

    // 9. Create a valid race
    @Test
    void createRace_withValidDates_savesSuccessfully() {
        when(raceRepository.save(any(Race.class))).thenReturn(draftRace);

        raceService.createRace(validRequest);

        verify(raceRepository).save(any(Race.class));
    }

    // 10. Reject a registration deadline that's after the scheduled start
    @Test
    void createRace_withDeadlineAfterStart_throwsException() {
        validRequest.setRegistrationDeadline(LocalDateTime.now().plusDays(31)); // after scheduledDateTime

        assertThrows(InvalidRaceDatesException.class,
                () -> raceService.createRace(validRequest));

        verify(raceRepository, never()).save(any());
    }

    // 11. Legal status transition succeeds
    @Test
    void updateRaceStatus_legalTransition_succeeds() {
        RaceStatusUpdateDto statusUpdate = new RaceStatusUpdateDto();
        statusUpdate.setStatus(RaceStatus.OPEN_FOR_REGISTRATION);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(draftRace));
        when(raceRepository.save(any(Race.class))).thenReturn(draftRace);

        raceService.updateRaceStatus(1L, statusUpdate);

        verify(raceRepository).save(any(Race.class));
    }

    // 12. Illegal status transition is rejected (a completed race cannot return to DRAFT)
    @Test
    void updateRaceStatus_illegalTransition_throwsException() {
        RaceStatusUpdateDto statusUpdate = new RaceStatusUpdateDto();
        statusUpdate.setStatus(RaceStatus.COMPLETED);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(draftRace)); // DRAFT -> COMPLETED is illegal

        assertThrows(InvalidRaceStatusTransitionException.class,
                () -> raceService.updateRaceStatus(1L, statusUpdate));

        verify(raceRepository, never()).save(any());
    }

    // 13. IN_PROGRESS requires at least 2 approved registrations
    @Test
    void updateRaceStatus_toInProgress_withInsufficientParticipants_throwsException() {
        Race closedRace = new Race();
        closedRace.setId(1L);
        closedRace.setStatus(RaceStatus.CLOSED_FOR_REGISTRATION);

        RaceStatusUpdateDto statusUpdate = new RaceStatusUpdateDto();
        statusUpdate.setStatus(RaceStatus.IN_PROGRESS);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(closedRace));
        when(raceRegistrationRepository.countByRaceIdAndStatus(1L, RegistrationStatus.APPROVED))
                .thenReturn(1L); // only 1, needs at least 2

        assertThrows(RuntimeException.class,
                () -> raceService.updateRaceStatus(1L, statusUpdate));

        verify(raceRepository, never()).save(any());
    }
}