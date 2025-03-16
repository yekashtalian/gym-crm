package org.example.gymcrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.BruteForceException;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.UserServiceException;
import org.example.gymcrm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserDao userDao;
  private final LoginAttemptService loginAttemptService;

  @Transactional
  @Override
  public void changePassword(String username, String oldPassword, String newPassword) {
    logger.info("Attempting to change password for user: {}", username);
    var existingUser =
        userDao
            .findByUsername(username)
            .orElseThrow(
                () -> {
                  logger.warn("User {} not found when attempting password change", username);
                  return new NotFoundException("This user doesn't exist");
                });

    if (!existingUser.getPassword().equals(oldPassword)) {
      logger.warn("Invalid old password provided for user: {}", username);
      throw new UserServiceException("Invalid old password");
    }
    if (oldPassword.equals(newPassword)) {
      logger.warn("User {} attempted to set a new password identical to the old one", username);
      throw new UserServiceException("New password must be different from the old one");
    }
    existingUser.setPassword(newPassword);
    logger.info("Password changed successfully for user: {}", username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (loginAttemptService.isBlocked()) {
      throw new AuthenticationException(
          "Your ip is blocked due to too many failed login attempts. Try again later.");
    }

    return userDao
        .findByUsername(username)
        .map(
            user ->
                new User(
                    user.getUsername(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))))
        .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
  }
}
