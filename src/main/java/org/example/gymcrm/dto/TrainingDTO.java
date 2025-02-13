package org.example.gymcrm.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
  private Long id;
  private String traineeUsername;
  private String trainerUsername;
  private String trainingName;
  private String trainingType;
  private Date date;
  private Integer duration;
}
