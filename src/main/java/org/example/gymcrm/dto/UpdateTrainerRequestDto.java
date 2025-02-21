package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTrainerRequestDto {
  @NotBlank(message = "Username field is required in request")
  private String username;
  @NotBlank(message = "First name field is required in request")
  private String firstName;
  @NotBlank(message = "Last name field is required in request")
  private String lastName;
  private Long specializationId;
  private boolean active;
}
