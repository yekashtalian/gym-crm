package org.example.gymcrm.service;

import org.example.gymcrm.dto.AuthenticationResponse;

public interface UserService {

  void changePassword(String username, String oldPassword, String newPassword);
  AuthenticationResponse login(String username, String password);
}
