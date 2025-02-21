package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTraineeRequestDto {
  @NotEmpty(message = "Trainee first name cannot be empty")
  private String firstName;

  @NotEmpty(message = "Trainee last name cannot be empty")
  private String lastName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

  private Date dateOfBirth;
  private String address;
}
