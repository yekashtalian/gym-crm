package org.example.gymcrm.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
  @NotEmpty(message = "Missing username for login")
  private String username;

  @NotEmpty(message = "Missing password for login")
  private String password;
}
