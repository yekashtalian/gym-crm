package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeTrainingDto {
  private String name;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date date;

  private TrainingTypeDto trainingType;

  private Integer duration;
  private String trainerName;
}
