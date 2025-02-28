package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerTrainingDto {
  private String name;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date date;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private TrainingTypeDto trainingType;

  private Integer duration;
  private String traineeName;
}
