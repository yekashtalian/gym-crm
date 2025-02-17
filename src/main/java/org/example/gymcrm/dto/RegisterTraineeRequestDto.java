package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTraineeRequestDto {
  @NotEmpty(message = "Trainee first name cannot be empty")
  private String firstName;

  @NotEmpty(message = "Trainee last name cannot be empty")
  private String lastName;

  private Date dateOfBirth;
  private String address;
}
