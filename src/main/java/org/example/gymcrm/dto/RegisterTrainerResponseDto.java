package org.example.gymcrm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTrainerResponseDto {
  private String username;
  private String password;
  private String token;

  public RegisterTrainerResponseDto(String username) {
    this.username = username;
  }
}
