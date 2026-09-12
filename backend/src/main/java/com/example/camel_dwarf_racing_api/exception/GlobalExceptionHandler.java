package com.example.camel_dwarf_racing_api.exception;

import com.example.camel_dwarf_racing_api.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(DuplicateNicknameException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateNickname(
                        DuplicateNicknameException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponseDto> handleValidationErrors(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String combinedMessage = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                .collect(Collectors.joining("; "));

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                combinedMessage,
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(CompetitorNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleNotFoundCompetitor(
                        CompetitorNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(TeamNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleNotFoundTeam(
                        TeamNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(DuplicateTeamNameException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateTeamName(
                        DuplicateTeamNameException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(DuplicateCoachException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateCoach(
                        DuplicateCoachException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(DuplicateTeamMembershipException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateTeamMembership(
                        DuplicateTeamMembershipException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(TeamCapacityExceededException.class)
        public ResponseEntity<ErrorResponseDto> handleTeamCapacity(
                        TeamCapacityExceededException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(CompetitorNotOnTeamException.class)
        public ResponseEntity<ErrorResponseDto> handleCompetitorNotOnTeam(
                        CompetitorNotOnTeamException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
}