package org.example.gymcrm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
  private String username;
  private String oldPassword;
  private String newPassword;
}
