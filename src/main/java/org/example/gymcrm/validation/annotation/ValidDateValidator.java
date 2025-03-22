package org.example.gymcrm.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ValidDateValidator implements ConstraintValidator<ValidDate, Date> {

  @Override
  public boolean isValid(Date value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    LocalDate localDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    return !localDate.isAfter(LocalDate.now());
  }
}
