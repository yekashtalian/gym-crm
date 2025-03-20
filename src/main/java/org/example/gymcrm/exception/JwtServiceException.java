package org.example.gymcrm.exception;

public class JwtServiceException extends RuntimeException {
  public JwtServiceException(String message) {
    super(message);
  }
}
