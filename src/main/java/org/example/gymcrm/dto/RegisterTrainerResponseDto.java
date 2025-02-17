package org.example.gymcrm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTrainerResponseDto {
  private String username;
  private String password;
}
