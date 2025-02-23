package org.example.gymcrm.web.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.example.gymcrm.dto.ErrorDetail;
import org.example.gymcrm.dto.ErrorResponse;
import org.example.gymcrm.exception.*;
import org.example.gymcrm.service.TrainingTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleInvalidArguments(MethodArgumentNotValidException ex) {
    var errorDetails =
        ex.getBindingResult().getFieldErrors().stream().map(ErrorDetail::new).toList();

    var errorResponse =
        new ErrorResponse(
            LocalDateTime.now(),
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
            .localDateTime(LocalDateTime.now())
            .errorMessage(
                ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(", ")))
            .build();
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(value = UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {
    var errorResponse =
        ErrorResponse.builder()
            .localDateTime(LocalDateTime.now())
            .errorMessage(ex.getMessage())
            .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
        ErrorResponse.builder()
            .localDateTime(LocalDateTime.now())
            .errorMessage(ex.getMessage())
            .build();
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
