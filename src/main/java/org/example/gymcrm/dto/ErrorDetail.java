package org.example.gymcrm.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;

@Getter
@Setter
public class ErrorDetail {
  private String field;
  private String message;
  private String rejectedValue;

  public ErrorDetail(FieldError fieldError) {
    this.field = fieldError.getField();
    this.message = fieldError.getDefaultMessage();
    this.rejectedValue =
        (fieldError.getRejectedValue() != null) ? fieldError.getRejectedValue().toString() : "null";
  }
}
