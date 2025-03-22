package org.example.gymcrm.web.controller;

import static org.springframework.security.core.context.SecurityContextHolder.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dto.AuthenticationResponse;
import org.example.gymcrm.dto.ChangePasswordRequest;
import org.example.gymcrm.dto.LoginDto;
import org.example.gymcrm.security.service.JwtBlacklistService;
import org.example.gymcrm.service.AuthService;
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
  private final JwtBlacklistService blacklistService;

  @PostMapping("/user/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDto loginDto) {
    var username = loginDto.getUsername();
    var password = loginDto.getPassword();
    var jwtToken = userService.login(username, password);
    return ResponseEntity.ok(jwtToken);
  }

  @PostMapping("/user/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.substring(7);
    blacklistService.blacklistToken(token);
    return ResponseEntity.ok("Logged out successfully");
  }

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
