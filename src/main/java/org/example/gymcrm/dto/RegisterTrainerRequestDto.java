package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTrainerRequestDto {
  @NotEmpty(message = "Trainer first name cannot be empty")
  private String firstName;

  @NotEmpty(message = "Trainer last name cannot be empty")
  private String lastName;

  @NotNull(message = "Trainer must have specialization id")
  private Long specializationId;
}
