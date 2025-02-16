package org.example.gymcrm.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean isActive;
  private Date dateOfBirth;
  private String address;
  private List<String> trainerUsernames;
}
