package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTrainerRequestDto {
  @NotEmpty(message = "Trainer first name cannot be empty")
  @Size(min = 2, max = 30, message = "Trainer first name should be from 2 to 30 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  private String firstName;

  @NotEmpty(message = "Trainer last name cannot be empty")
  @Size(min = 2, max = 35, message = "Trainer last name size should be from 2 to 35 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  private String lastName;

  @NotNull(message = "Trainer must have specialization id")
  private Long specializationId;
}
