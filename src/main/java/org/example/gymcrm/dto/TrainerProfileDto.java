package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileDto {
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String username;
  private String firstName;
  private String lastName;
  private Long specialization;
  private boolean active;
  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private List<TrainerTraineesDto> trainees;
}
