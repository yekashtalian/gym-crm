package org.example.gymcrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.dto.AuthenticationResponse;
import org.example.gymcrm.exception.BruteForceException;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.UnauthorizedException;
import org.example.gymcrm.exception.UserServiceException;
import org.example.gymcrm.security.service.JwtService;
import org.example.gymcrm.service.AuthService;
import org.example.gymcrm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserDao userDao;
  private final AuthService authService;
  private final JwtService jwtService;
  private final LoginAttemptService loginAttemptService;
  private final PasswordEncoder passwordEncoder;

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

    if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
      logger.warn("Invalid old password provided for user: {}", username);
      throw new UserServiceException("Invalid old password");
    }

    if (passwordEncoder.matches(newPassword, existingUser.getPassword())) {
      logger.warn("User {} attempted to set a new password identical to the old one", username);
      throw new UserServiceException("New password must be different from the old one");
    }
    existingUser.setPassword(passwordEncoder.encode(newPassword));
    logger.info("Password changed successfully for user: {}", username);
  }

  @Transactional
  @Override
  public AuthenticationResponse login(String username, String password) {
    logger.info("Trying to login user {} in system....", username);
    var isAuthenticated = authService.authenticate(username, password);
    if (loginAttemptService.isBlocked()){
      throw new BruteForceException("Your ip is blocked due to so many failed login attempts. Try again later.");
    }
    if (!isAuthenticated) {
      throw new UnauthorizedException("Invalid authentication");
    }

    var user =
        userDao.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    var jwt = jwtService.generateToken(user);
    logger.info("User successfully logged in");

    return AuthenticationResponse.builder().token(jwt).build();
  }
}
