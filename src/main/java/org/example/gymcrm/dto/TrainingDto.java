package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingDto {
  @NotBlank(message = "Missing trainee username")
  private String traineeUsername;

  @NotBlank(message = "Missing trainer username")
  private String trainerUsername;

  @NotBlank(message = "Missing training name")
  private String trainingName;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date trainingDate;

  private Integer duration;
}
