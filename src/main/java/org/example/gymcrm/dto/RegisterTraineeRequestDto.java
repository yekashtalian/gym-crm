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
public class RegisterTraineeRequestDto {
  @NotEmpty(message = "Trainee first name cannot be empty")
  @Size(min = 2, max = 30, message = "Trainee first name should be from 2 to 30 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  private String firstName;

  @NotEmpty(message = "Trainee last name cannot be empty")
  @Size(min = 2, max = 35, message = "Trainee last name size should be from 2 to 35 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  private String lastName;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @ValidDate
  private Date dateOfBirth;

  private String address;
}
