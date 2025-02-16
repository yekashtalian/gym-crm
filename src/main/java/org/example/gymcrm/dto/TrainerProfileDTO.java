package org.example.gymcrm.dto;

import lombok.*;
import org.example.gymcrm.entity.TrainingType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean isActive;
  private String specialization;
}
