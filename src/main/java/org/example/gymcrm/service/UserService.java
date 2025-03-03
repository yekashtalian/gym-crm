package org.example.gymcrm.service;

public interface UserService {
  boolean validateCredentials(String username, String password);

  void changePassword(String username, String oldPassword, String newPassword);
}
