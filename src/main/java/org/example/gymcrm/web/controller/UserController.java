package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.ChangePasswordRequest;
import org.example.gymcrm.dto.LoginDto;
import org.example.gymcrm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class UserController {
  private final UserService userService;

  @PostMapping("/user/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
    var username = loginDto.getUsername();
    var password = loginDto.getPassword();

    var isValid = userService.validateCredentials(username, password);

    if (isValid) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!");
    }
  }

  @RequiresAuthentication
  @PutMapping("/user/password")
  public ResponseEntity<Void> changePassword(
      @RequestBody @Valid ChangePasswordRequest credentials) {
    var username = credentials.getUsername();
    var oldPassword = credentials.getOldPassword();
    var newPassword = credentials.getNewPassword();

    userService.changePassword(username, oldPassword, newPassword);

    return ResponseEntity.ok().build();
  }
}
