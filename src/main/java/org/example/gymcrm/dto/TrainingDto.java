package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.gymcrm.validation.annotation.ValidDate;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingDto {
  @NotEmpty(message = "Missing trainee username")
  private String traineeUsername;

  @NotEmpty(message = "Missing trainer username")
  private String trainerUsername;

  @NotEmpty(message = "Missing training name")
  @Size(min = 5, max = 20, message = "Training name size should be from 5 to 20 symbols")
  private String trainingName;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @NotEmpty(message = "Missing training date field")
  private Date trainingDate;

  @NotNull(message = "Training duration cannot be null")
  @Positive(message = "Training duration cannot be negative number")
  @Min(value = 25, message = "Training duration should be longer than 25 minutes")
  @Max(value = 90, message = "Training duration should be shorter than 90 minutes")
  private Integer duration;
}
