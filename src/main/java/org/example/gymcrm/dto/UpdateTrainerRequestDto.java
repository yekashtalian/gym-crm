package org.example.gymcrm.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTrainerRequestDto {
  @NotBlank(message = "First name field is required in request")
  @Size(min = 2, max = 30, message = "Trainer first name should be from 2 to 30 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  private String firstName;

  @NotBlank(message = "Last name field is required in request")
  @Size(min = 2, max = 35, message = "Trainer last name size should be from 2 to 35 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  private String lastName;

  private Long specializationId;

  @NotNull(message = "Active field cannot be empty")
  private Boolean active;
}
