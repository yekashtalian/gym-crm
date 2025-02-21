package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeProfileDto {
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String username;

  private String firstName;
  private String lastName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date dateOfBirth;
  private String address;
  private boolean active;

  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private List<TraineeTrainersDto> trainers;
}
