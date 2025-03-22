package org.example.gymcrm.web.controller;

import org.example.gymcrm.dto.AuthenticationResponse;
import org.example.gymcrm.dto.ChangePasswordRequest;
import org.example.gymcrm.dto.LoginDto;
import org.example.gymcrm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void changePasswordTest() {
    ChangePasswordRequest request = new ChangePasswordRequest("testUser", "oldPass", "newPass");

    doNothing().when(userService).changePassword("testUser", "oldPass", "newPass");

    ResponseEntity<Void> response = userController.changePassword(request);

    assertEquals(200, response.getStatusCodeValue());
    verify(userService).changePassword("testUser", "oldPass", "newPass");
  }

  @Test
  void loginTest() {
    LoginDto loginDto = new LoginDto("testUser", "password");

    ResponseEntity<AuthenticationResponse> response = userController.login(loginDto);

    assertEquals(200, response.getStatusCodeValue());
  }
}
