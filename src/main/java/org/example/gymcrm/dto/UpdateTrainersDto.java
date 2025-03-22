package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainersDto {
  @NotEmpty(message = "Trainee trainers are required")
  private List<String> trainers;
}
