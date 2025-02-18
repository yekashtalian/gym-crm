package org.example.gymcrm.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.ChangePasswordRequest;
import org.example.gymcrm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/user/login")
  public ResponseEntity<?> login(
      @RequestParam(name = "username") String username,
      @RequestParam(name = "password") String password) {
    var isValid = userService.validateCredentials(username, password);

    if (isValid) {
      return ResponseEntity.status(HttpStatus.OK).body(username + " " + password);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
    }
  }

  @RequiresAuthentication
  @PutMapping("/user/change-password")
  public ResponseEntity<?> changePassword(
      @RequestBody ChangePasswordRequest credentials) {
    var username = credentials.getUsername();
    var oldPassword = credentials.getOldPassword();
    var newPassword = credentials.getNewPassword();

    userService.changePassword(username, oldPassword, newPassword);

    return ResponseEntity.status(HttpStatus.OK)
        .body("Successfully changed old password to: " + newPassword);
  }
}
