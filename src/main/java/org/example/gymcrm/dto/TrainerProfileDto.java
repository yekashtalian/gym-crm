package org.example.gymcrm.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDto {
  private String firstName;
  private String lastName;
  private Long specialization;
  private boolean active;
  private List<TrainerTraineesDto> trainees;
}
