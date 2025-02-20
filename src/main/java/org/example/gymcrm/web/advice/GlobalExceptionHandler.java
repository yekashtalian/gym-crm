package org.example.gymcrm.web.advice;

import org.example.gymcrm.dto.ErrorResponse;
import org.example.gymcrm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
        TrainingServiceException.class
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
