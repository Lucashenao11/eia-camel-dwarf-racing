package com.example.camel_dwarf_racing_api.exception;

import com.example.camel_dwarf_racing_api.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
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

        @ExceptionHandler(RaceNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleRaceNotFound(RaceNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(InvalidRaceDatesException.class)
        public ResponseEntity<ErrorResponseDto> handleInvalidRaceDates(InvalidRaceDatesException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(RaceAlreadyCompletedException.class)
        public ResponseEntity<ErrorResponseDto> handleRaceAlreadyCompleted(RaceAlreadyCompletedException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(InvalidRaceStatusTransitionException.class)
        public ResponseEntity<ErrorResponseDto> handleInvalidRaceStatusTransition(
                        InvalidRaceStatusTransitionException ex, HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponseDto> handleMalformedJson(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "The request body is malformed or contains invalid values.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(RegistrationNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleRegistrationNotFound(RegistrationNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(InvalidRegistrationRequestException.class)
        public ResponseEntity<ErrorResponseDto> handleInvalidRegistrationRequest(InvalidRegistrationRequestException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(RegistrationClosedException.class)
        public ResponseEntity<ErrorResponseDto> handleRegistrationClosed(RegistrationClosedException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(DuplicateRegistrationException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateRegistration(DuplicateRegistrationException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(IneligibleParticipantException.class)
        public ResponseEntity<ErrorResponseDto> handleIneligibleParticipant(IneligibleParticipantException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(RegistrationAlreadyProcessedException.class)
        public ResponseEntity<ErrorResponseDto> handleRegistrationAlreadyProcessed(
                        RegistrationAlreadyProcessedException ex, HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleNoResourceFound(
                        NoResourceFoundException ex,
                        HttpServletRequest request) {

                ErrorResponseDto error = new ErrorResponseDto(
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                "The requested endpoint does not exist.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(ResultNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleResultNotFound(ResultNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(RaceNotInProgressException.class)
        public ResponseEntity<ErrorResponseDto> handleRaceNotInProgress(RaceNotInProgressException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(RegistrationNotApprovedException.class)
        public ResponseEntity<ErrorResponseDto> handleRegistrationNotApproved(RegistrationNotApprovedException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(DuplicateResultException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateResult(DuplicateResultException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(InvalidResultDataException.class)
        public ResponseEntity<ErrorResponseDto> handleInvalidResultData(InvalidResultDataException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(DuplicateFinalPositionException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateFinalPosition(DuplicateFinalPositionException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(InsufficientParticipantsException.class)
        public ResponseEntity<ErrorResponseDto> handleInsufficientParticipants(InsufficientParticipantsException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                "Invalid username or password.", request.getRequestURI());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(DuplicateUsernameException.class)
        public ResponseEntity<ErrorResponseDto> handleDuplicateUsername(DuplicateUsernameException ex,
                        HttpServletRequest request) {
                ErrorResponseDto error = new ErrorResponseDto(HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
}