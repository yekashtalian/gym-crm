package org.example.gymcrm.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingDto {
  private String name;
  private Date date;
  private TrainingTypeDto trainingType;
  private Integer duration;
  private String traineeName;
}
