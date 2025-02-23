package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
  @NotEmpty(message = "Missing username for password change")
  private String username;

  @NotEmpty(message = "Missing current user password for password change")
  private String oldPassword;

  @NotEmpty(message = "Missing new user password for password change")
  private String newPassword;
}
