package org.example.gymcrm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean isActive;
  private String specialization;
}
