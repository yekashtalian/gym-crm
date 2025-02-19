package org.example.gymcrm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerRequestDto {
  private String username;
  private String firstName;
  private String lastName;
  private Long specializationId;
  private boolean active;
}
