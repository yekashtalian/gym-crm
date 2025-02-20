package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
  @NotBlank(message = "Missing trainee username")
  private String traineeUsername;

  @NotBlank(message = "Missing trainer username")
  private String trainerUsername;

  @NotBlank(message = "Missing training name")
  private String trainingName;

  private Date trainingDate;

  private Integer duration;
}
