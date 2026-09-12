package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.CompetitorRequestDto;
import com.example.camel_dwarf_racing_api.dto.CompetitorResponseDto;
import com.example.camel_dwarf_racing_api.dto.CompetitorStatusUpdateDto;
import com.example.camel_dwarf_racing_api.exception.*;
import com.example.camel_dwarf_racing_api.model.Competitor;
import com.example.camel_dwarf_racing_api.model.CompetitorStatus;
import com.example.camel_dwarf_racing_api.model.CompetitorType;
import com.example.camel_dwarf_racing_api.repository.CompetitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    public CompetitorResponseDto createCompetitor(CompetitorRequestDto requestDto) {
        if (competitorRepository.existsByNickname(requestDto.getNickname())) {
            throw new DuplicateNicknameException(requestDto.getNickname());
        }

        Competitor competitor = new Competitor();
        competitor.setName(requestDto.getName());
        competitor.setNickname(requestDto.getNickname());
        competitor.setType(requestDto.getType());
        competitor.setDateOfBirth(requestDto.getDateOfBirth());
        competitor.setWeight(requestDto.getWeight());
        competitor.setHeight(requestDto.getHeight());
        competitor.setCountryOfOrigin(requestDto.getCountryOfOrigin());

        Competitor saved = competitorRepository.save(competitor);

        return toResponseDto(saved);
    }

    private CompetitorResponseDto toResponseDto(Competitor competitor) {
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

    public Page<CompetitorResponseDto> getAllCompetitors(
            CompetitorType type, CompetitorStatus status, Pageable pageable) {

        Page<Competitor> competitors;

        if (type != null && status != null) {
            competitors = competitorRepository.findByTypeAndStatus(type, status, pageable);
        } else if (type != null) {
            competitors = competitorRepository.findByType(type, pageable);
        } else if (status != null) {
            competitors = competitorRepository.findByStatus(status, pageable);
        } else {
            competitors = competitorRepository.findAll(pageable);
        }

        return competitors.map(this::toResponseDto);
    }

    public CompetitorResponseDto getCompetitorById(Long id) {
        Competitor competitor = competitorRepository.findById(id)
            .orElseThrow(() -> new CompetitorNotFoundException(id));
        return toResponseDto(competitor);
    }

    public CompetitorResponseDto updateCompetitor(Long id, CompetitorRequestDto requestDto) {

        Competitor competitor = competitorRepository.findById(id)
                .orElseThrow(() -> new CompetitorNotFoundException(id));
        String newNickname = requestDto.getNickname();
        if (competitorRepository.existsByNicknameAndIdNot(newNickname, id)) {
            throw new DuplicateNicknameException(newNickname);
        }

        competitor.setName(newNickname);
        competitor.setNickname(requestDto.getNickname());
        competitor.setWeight(requestDto.getWeight());
        competitor.setHeight(requestDto.getHeight());
        // type, dateOfBirth, and countryOfOrigin are immutable — intentionally not updated here

        Competitor saved = competitorRepository.save(competitor);
        return toResponseDto(saved);
    }

    public CompetitorResponseDto updateCompetitorStatus(Long id, CompetitorStatusUpdateDto requestDto) {

        Competitor competitor = competitorRepository.findById(id)
            .orElseThrow(() -> new CompetitorNotFoundException(id));
        
        competitor.setStatus(requestDto.getStatus());
        competitor = competitorRepository.save(competitor);
        return toResponseDto(competitor);
    }

    public void deleteCompetitor(Long id) {
        Competitor competitor = competitorRepository.findById(id)
                .orElseThrow(() -> new CompetitorNotFoundException(id));

        // TODO (Module 6): once RaceResult exists, check raceResultRepository.existsByCompetitorId(id)
        // and throw a business-conflict exception (409) instead of deleting, per the spec's rule
        // that a competitor with official results cannot be physically deleted.

        competitorRepository.delete(competitor);
    }
}