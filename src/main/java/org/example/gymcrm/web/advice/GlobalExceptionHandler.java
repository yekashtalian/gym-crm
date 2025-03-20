package org.example.gymcrm.web.advice;

import static java.time.LocalDateTime.*;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.gymcrm.dto.ErrorDetail;
import org.example.gymcrm.dto.ErrorResponse;
import org.example.gymcrm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArguments(
            MethodArgumentNotValidException ex) {
        var errorDetails =
                ex.getBindingResult().getFieldErrors().stream().map(ErrorDetail::new).toList();

        var errorResponse =
                new ErrorResponse(
                        now(),
                        String.format(
                                "Failed to create a new %s, the request contains invalid fields",
                                ex.getObjectName()),
                        errorDetails);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handlePathVariablesInvalidArguments(
            ConstraintViolationException ex) {
        var errorResponse =
                ErrorResponse.builder()
                        .localDateTime(now())
                        .errorMessage(
                                ex.getConstraintViolations().stream()
                                        .map(ConstraintViolation::getMessage)
                                        .sorted()
                                        .collect(Collectors.joining(", ")))
                        .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(
            value = {
                UserServiceException.class,
                TraineeServiceException.class,
                TrainerServiceException.class,
                TrainingServiceException.class,
                UserServiceException.class
            })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        var errorResponse =
                ErrorResponse.builder().localDateTime(now()).errorMessage(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(
            HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid request format. Please check your input.";
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType() == java.util.Date.class) {
                errorMessage = "Invalid date format. Use yyyy-MM-dd.";
            }
        }
        var errorResponse =
                ErrorResponse.builder().localDateTime(now()).errorMessage(errorMessage).build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(JwtServiceException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtServiceException e) {
        log.info("Jwt exception raised");
        var errorResponse =
                ErrorResponse.builder().localDateTime(now()).errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingAuthHeaderException(
            MissingRequestHeaderException ex) {
        log.info("Missing header exception raised");
        var errorResponse =
                ErrorResponse.builder().localDateTime(now()).errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BruteForceException.class)
    public ResponseEntity<ErrorResponse> handleBruteForceException(BruteForceException ex) {
        log.info("Brute force exception raised");
        var errorResponse =
                ErrorResponse.builder().localDateTime(now()).errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(BadCredentialsException ex) {
        log.info("Bad credentials exception raised");
        var errorResponse =
                ErrorResponse.builder().localDateTime(now()).errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
