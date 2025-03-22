package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
  @NotNull(message = "Username cannot be null")
  @NotEmpty(message = "Missing username for login")
  private String username;

  @NotNull(message = "Password cannot be null")
  @NotEmpty(message = "Missing password for login")
  private String password;
}
