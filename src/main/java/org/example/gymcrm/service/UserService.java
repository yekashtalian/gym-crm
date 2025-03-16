package org.example.gymcrm.service;

public interface UserService {

  void changePassword(String username, String oldPassword, String newPassword);
}
