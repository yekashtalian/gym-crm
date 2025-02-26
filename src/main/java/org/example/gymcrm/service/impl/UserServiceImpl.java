package org.example.gymcrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.UserServiceException;
import org.example.gymcrm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserDao userDao;

  @Transactional
  @Override
  public boolean validateCredentials(String username, String password) {
    var isAuthenticated =
        userDao.findByUsername(username).map(u -> u.getPassword().equals(password)).orElse(false);
    return isAuthenticated;
  }

  @Transactional
  @Override
  public void changePassword(String username, String oldPassword, String newPassword) {
    var existingUser =
        userDao
            .findByUsername(username)
            .orElseThrow(() -> new NotFoundException("This user doesn't exist"));

    if (!existingUser.getPassword().equals(oldPassword)) {
      throw new UserServiceException("Invalid old password");
    }
    if (oldPassword.equals(newPassword)) {
      throw new UserServiceException("New password must be different from the old one");
    }
    existingUser.setPassword(newPassword);
  }
}
