package org.example.gymcrm.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.ChangePasswordRequest;
import org.example.gymcrm.dto.LoginDto;
import org.example.gymcrm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class UserControllerTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void login_ValidCredentials_ReturnsOk() throws Exception {
    var loginDto = new LoginDto("testUser", "testPassword");
    var username = loginDto.getUsername();
    var password = loginDto.getPassword();

    var loginJson = objectMapper.writeValueAsString(loginDto);

    when(userService.validateCredentials(username, password)).thenReturn(true);

    mockMvc
        .perform(
            post("/api/v1/user/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
        .andExpect(status().isOk());

    verify(userService, times(1)).validateCredentials(username, password);
  }

  @Test
  public void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
    var loginDto = new LoginDto("testUser", "wrongPassword");
    var username = loginDto.getUsername();
    var password = loginDto.getPassword();

    var loginJson = objectMapper.writeValueAsString(loginDto);

    when(userService.validateCredentials(username, password)).thenReturn(false);

    mockMvc
        .perform(
            post("/api/v1/user/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Invalid credentials!"));

    verify(userService, times(1)).validateCredentials(username, password);
  }

  @Test
  public void changePassword_Success_ReturnsOk() throws Exception {
    ChangePasswordRequest changePasswordRequest =
        new ChangePasswordRequest("testUser", "oldPassword", "newPassword");

    mockMvc
        .perform(
            put("/api/v1/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
        .andExpect(status().isOk());

    verify(userService, times(1)).changePassword("testUser", "oldPassword", "newPassword");
  }
}
