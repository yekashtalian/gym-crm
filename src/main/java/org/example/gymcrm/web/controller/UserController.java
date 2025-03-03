package org.example.gymcrm.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
    name = "User Controller",
    description = "Endpoints for user authentication and password management")
public class UserController {
  private final UserService userService;

  @PostMapping("/user/login")
  @Operation(
      summary = "User login",
      description = "Authenticate a user with their username and password",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "User credentials for authentication",
              content =
                  @Content(
                      mediaType = "application/json",
                      examples =
                          @ExampleObject(
                              value =
                                  """
                        {
                            "username": "john.doe",
                            "password": "password123"
                        }
                        """))))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(mediaType = "application/json")),
    @ApiResponse(
        responseCode = "401",
        description = "Invalid credentials",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Invalid credentials!"
                        }
                        """)))
  })
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
  @Operation(
      summary = "Change user password",
      description = "Change the password for an authenticated user",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Password change request details",
              content =
                  @Content(
                      mediaType = "application/json",
                      examples =
                          @ExampleObject(
                              value =
                                  """
                        {
                            "username": "john.doe",
                            "oldPassword": "password123",
                            "newPassword": "newPassword123"
                        }
                        """))),
      parameters = {
        @Parameter(
            name = "Username",
            description = "Trainee's username for authentication",
            example = "yevhenii.kashtalian",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "Password",
            description = "Trainee's password for authentication",
            example = "qwerty1234",
            in = ParameterIn.HEADER,
            required = true)
      })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Password changed successfully",
        content = @Content(mediaType = "application/json")),
    @ApiResponse(
        responseCode = "401",
        description = "Missing authentication headers response",
        content =
            @Content(
                mediaType = "application/json",
                examples = {
                  @ExampleObject(
                      name = "Missing headers",
                      description = "Missing authentication headers",
                      value =
                          """
                                                                  {
                                                                  "localDateTime": "2024-08-05T16:16:53.8490207",
                                                                  "errorMessage": "Missing authentication headers"
                                                                  }
                                                                  """),
                  @ExampleObject(
                      name = "Invalid headers",
                      description = "Invalid user credentials",
                      value =
                          """
                                                                           {
                                                                           "localDateTime": "2024-08-05T16:16:53.8490207",
                                                                           "errorMessage": "Invalid credentials"
                                                                           }
                                                                           """)
                })),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request body",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Failed to change password, the request contains invalid fields",
                            "errors": [
                                {
                                    "field": "oldPassword",
                                    "message": "Old password cannot be empty",
                                    "rejectedValue": ""
                                }
                            ]
                        }
                        """)))
  })
  public ResponseEntity<Void> changePassword(
      @RequestBody @Valid ChangePasswordRequest credentials) {
    var username = credentials.getUsername();
    var oldPassword = credentials.getOldPassword();
    var newPassword = credentials.getNewPassword();

    userService.changePassword(username, oldPassword, newPassword);

    return ResponseEntity.ok().build();
  }
}
