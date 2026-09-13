package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.CompetitorRequestDto;
import com.example.camel_dwarf_racing_api.dto.CompetitorResponseDto;
import com.example.camel_dwarf_racing_api.dto.CompetitorStatusUpdateDto;
import com.example.camel_dwarf_racing_api.exception.CompetitorNotFoundException;
import com.example.camel_dwarf_racing_api.exception.DuplicateNicknameException;
import com.example.camel_dwarf_racing_api.model.Competitor;
import com.example.camel_dwarf_racing_api.model.CompetitorStatus;
import com.example.camel_dwarf_racing_api.model.CompetitorType;
import com.example.camel_dwarf_racing_api.repository.CompetitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitorServiceTest {

    @Mock
    private CompetitorRepository competitorRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private CompetitorService competitorService;

    private CompetitorRequestDto validRequest;
    private Competitor savedCompetitor;

    @BeforeEach
    void setUp() {
        validRequest = new CompetitorRequestDto();
        validRequest.setName("Byte");
        validRequest.setNickname("Byte the Camel");
        validRequest.setType(CompetitorType.CAMEL);
        validRequest.setDateOfBirth(LocalDate.of(2019, 5, 10));
        validRequest.setWeight(480.5);
        validRequest.setHeight(210.0);
        validRequest.setCountryOfOrigin("Colombia");

        savedCompetitor = new Competitor();
        savedCompetitor.setId(1L);
        savedCompetitor.setName("Byte");
        savedCompetitor.setNickname("Byte the Camel");
        savedCompetitor.setType(CompetitorType.CAMEL);
        savedCompetitor.setDateOfBirth(LocalDate.of(2019, 5, 10));
        savedCompetitor.setWeight(480.5);
        savedCompetitor.setHeight(210.0);
        savedCompetitor.setCountryOfOrigin("Colombia");
        savedCompetitor.setStatus(CompetitorStatus.ACTIVE);
        savedCompetitor.setRegistrationDate(LocalDate.now());
    }

    // 1. Create a valid competitor
    @Test
    void createCompetitor_withValidData_savesAndReturnsCompetitor() {
        when(competitorRepository.existsByNickname("Byte the Camel")).thenReturn(false);
        when(competitorRepository.save(any(Competitor.class))).thenReturn(savedCompetitor);

        CompetitorResponseDto result = competitorService.createCompetitor(validRequest);

        assertThat(result.getNickname()).isEqualTo("Byte the Camel");
        assertThat(result.getStatus()).isEqualTo(CompetitorStatus.ACTIVE);
        verify(competitorRepository).save(any(Competitor.class));
    }

    // 2. Reject a duplicated nickname
    @Test
    void createCompetitor_withDuplicateNickname_throwsException() {
        when(competitorRepository.existsByNickname("Byte the Camel")).thenReturn(true);

        assertThrows(DuplicateNicknameException.class,
                () -> competitorService.createCompetitor(validRequest));

        verify(competitorRepository, never()).save(any());
    }

    // 3. Get an existing competitor by id
    @Test
    void getCompetitorById_whenExists_returnsCompetitor() {
        when(competitorRepository.findById(1L)).thenReturn(Optional.of(savedCompetitor));

        CompetitorResponseDto result = competitorService.getCompetitorById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNickname()).isEqualTo("Byte the Camel");
    }

    // 4. Return 404-equivalent for a missing resource
    @Test
    void getCompetitorById_whenNotFound_throwsException() {
        when(competitorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CompetitorNotFoundException.class,
                () -> competitorService.getCompetitorById(999L));
    }

    // 5. Update leaves immutable fields (type, dateOfBirth, countryOfOrigin) untouched
    @Test
    void updateCompetitor_doesNotChangeImmutableFields() {
        CompetitorRequestDto updateRequest = new CompetitorRequestDto();
        updateRequest.setName("Byte");
        updateRequest.setNickname("Byte the Camel");
        updateRequest.setType(CompetitorType.DWARF); // attempted change — must be ignored
        updateRequest.setDateOfBirth(LocalDate.of(2000, 1, 1)); // attempted change — must be ignored
        updateRequest.setWeight(500.0);
        updateRequest.setHeight(215.0);
        updateRequest.setCountryOfOrigin("Wonderland"); // attempted change — must be ignored

        when(competitorRepository.findById(1L)).thenReturn(Optional.of(savedCompetitor));
        when(competitorRepository.existsByNicknameAndIdNot("Byte the Camel", 1L)).thenReturn(false);
        when(competitorRepository.save(any(Competitor.class))).thenReturn(savedCompetitor);

        competitorService.updateCompetitor(1L, updateRequest);

        assertThat(savedCompetitor.getType()).isEqualTo(CompetitorType.CAMEL);
        assertThat(savedCompetitor.getCountryOfOrigin()).isEqualTo("Colombia");
        assertThat(savedCompetitor.getWeight()).isEqualTo(500.0); // mutable field DOES change
    }

    // 6. Status update changes status correctly
    @Test
    void updateCompetitorStatus_setsNewStatus() {
        CompetitorStatusUpdateDto statusUpdate = new CompetitorStatusUpdateDto();
        statusUpdate.setStatus(CompetitorStatus.INJURED);

        when(competitorRepository.findById(1L)).thenReturn(Optional.of(savedCompetitor));
        when(competitorRepository.save(any(Competitor.class))).thenReturn(savedCompetitor);

        CompetitorResponseDto result = competitorService.updateCompetitorStatus(1L, statusUpdate);

        assertThat(result.getStatus()).isEqualTo(CompetitorStatus.INJURED);
    }

    // 7. Delete calls the repository when the competitor exists
    @Test
    void deleteCompetitor_whenExists_deletesSuccessfully() {
        when(competitorRepository.findById(1L)).thenReturn(Optional.of(savedCompetitor));

        competitorService.deleteCompetitor(1L);

        verify(competitorRepository).delete(savedCompetitor);
    }

    // 8. Delete on a missing competitor throws instead of silently doing nothing
    @Test
    void deleteCompetitor_whenNotFound_throwsException() {
        when(competitorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CompetitorNotFoundException.class,
                () -> competitorService.deleteCompetitor(999L));

        verify(competitorRepository, never()).delete(any());
    }
}