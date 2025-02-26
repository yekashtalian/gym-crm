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
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "Training Controller", description = "Endpoints for managing training sessions")
public class TrainingController {
  private final TrainingService trainingService;

  @RequiresAuthentication
  @PostMapping("/training")
  @Operation(
      summary = "Create a new training session",
      description = "Adds a new training session to the system",
      parameters = {
        @Parameter(
            name = "Username",
            description = "User's username for authentication",
            example = "john.doe",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "Password",
            description = "User's password for authentication",
            example = "password123",
            in = ParameterIn.HEADER,
            required = true)
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Training session details to be saved",
              content =
                  @Content(
                      mediaType = "application/json",
                      examples =
                          @ExampleObject(
                              value =
                                  """
                        {
                            "traineeUsername": "john.doe",
                            "trainerUsername": "jane.doe",
                            "trainingName": "Morning Cardio",
                            "trainingDate": "2024-12-01",
                            "trainingDuration": 60,
                            "trainingTypeId": 1
                        }
                        """))))
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Training session successfully created"),
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
                        """))),
    @ApiResponse(
        responseCode = "401",
        description = "Missing authentication headers response",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                          {
                                          "localDateTime": "2024-08-05T16:16:53.8490207",
                                          "errorMessage": "Missing authentication headers"
                                          }
                                          """))),
    @ApiResponse(
        responseCode = "401",
        description = "Invalid username or password headers",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                           {
                                           "localDateTime": "2024-08-05T16:16:53.8490207",
                                           "errorMessage": "Invalid credentials"
                                           }
                                           """)))
  })
  public ResponseEntity<Void> saveTraining(@RequestBody @Valid TrainingDto training) {
    trainingService.save(training);
    return ResponseEntity.ok().build();
  }
}
