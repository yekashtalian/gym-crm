package org.example.gymcrm.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileDto {
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private String address;
  private boolean active;
  private List<TraineeTrainersDto> trainers;
}
