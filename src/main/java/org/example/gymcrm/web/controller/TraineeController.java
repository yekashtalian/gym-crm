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
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Trainee Controller", description = "Endpoints for managing trainees")
public class TraineeController {
  private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
  private final TraineeService traineeService;
  private final TrainingService trainingService;

  @RequiresAuthentication
  @GetMapping("/trainee/{username}")
  @Operation(
      summary = "Get trainee by username",
      description = "Fetch trainee details by username",
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
            example = "b9T(ac^ydY",
            in = ParameterIn.HEADER,
            required = true)
      })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                            {
                            "firstName": "John",
                            "lastName": "Doe",
                            "dateOfBirth": "1990-01-01",
                            "address": "123 Main St",
                            "active": true,
                            "trainers": [
                                {
                                "username": "john.doe",
                                "firstName": "John",
                                "lastName": "Doe",
                                "specializationId": "3"
                                }
                            ]
                            }
                            """))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid username format",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                            {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Invalid format. Use two English words separated by a dot (e.g., john.doe)."
                            }
                            """))),
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
    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content())
  })
  public ResponseEntity<TraineeProfileDto> getTrainee(
      @PathVariable("username")
          @Parameter(description = "Username of the trainee", example = "yevhenii.kashtalian")
          String username) {
    var traineeProfile = traineeService.findByUsername(username);
    return ResponseEntity.ok(traineeProfile);
  }

  @PostMapping("/trainee")
  @Operation(summary = "Register a new trainee", description = "Adds a new trainee to the system")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully registered",
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
                            """))),
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
                            "errorMessage": "Failed to create a new trainee, the request contains invalid fields",
                            "errors": [
                            {
                            "field": "firstName",
                            "message": "First name cannot be empty",
                            "rejectedValue": ""
                            }
                            ]
                            }
                            """)))
  })
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Trainee to save in the gym",
      content =
          @Content(
              mediaType = "application/json",
              examples =
                  @ExampleObject(
                      value =
                          """
                          {
                          "firstName": "Yevhenii",
                          "lastName": "Kashtalian",
                          "dateOfBirth": "1990-01-01",
                          "address": "123 Main St"
                          }
                          """)))
  public ResponseEntity<RegisterTraineeResponseDto> registerTrainee(
      @RequestBody @Valid RegisterTraineeRequestDto trainee) {
    var registeredTrainee = traineeService.save(trainee);
    return ResponseEntity.ok(registeredTrainee);
  }

  @RequiresAuthentication
  @PutMapping("/trainee/{username}")
  @Operation(
      summary = "Update trainee details",
      description = "Update trainee information",
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
            example = "b9T(ac^ydY",
            in = ParameterIn.HEADER,
            required = true)
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Trainee to update in the gym",
              content =
                  @Content(
                      mediaType = "application/json",
                      examples =
                          @ExampleObject(
                              value =
                                  """
                                          {
                                          "firstName": "Yevhenii",
                                          "lastName": "Kashtalian",
                                          "dateOfBirth": "1990-01-01",
                                          "address": "123 Main St",
                                          "active": "false"
                                          }
                                          """))))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully updated",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                            {
                            "firstName": "John",
                            "lastName": "Doe",
                            "username": "john.doe",
                            "dateOfBirth": "1990-01-01",
                            "address": "123 Main St",
                            "isActive": true
                            }
                            """))),
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
                                              "errorMessage": "Failed to update trainee, the request contains invalid fields",
                                              "errors": [
                                                {
                                                  "field": "firstName",
                                                  "message": "First name cannot be empty",
                                                  "rejectedValue": ""
                                                }
                                              ]
                                            }
                                            """))),
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
    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content())
  })
  public ResponseEntity<TraineeProfileDto> updateTrainee(
      @PathVariable("username") String username,
      @RequestBody @Valid UpdateTraineeRequestDto trainee) {
    var traineeProfile = traineeService.update(username, trainee);
    return ResponseEntity.ok(traineeProfile);
  }

  @RequiresAuthentication
  @DeleteMapping("/trainee/{username}")
  @Operation(
      summary = "Delete a trainee",
      description = "Remove a trainee from the system",
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
            example = "b9T(ac^ydY",
            in = ParameterIn.HEADER,
            required = true)
      })
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully deleted"),
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
    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content())
  })
  public ResponseEntity<Void> deleteTrainee(
      @PathVariable("username")
          @Parameter(description = "Username of the trainee", example = "john.doe")
          String username) {
    traineeService.deleteByUsername(username);
    return ResponseEntity.ok().build();
  }

  @RequiresAuthentication
  @GetMapping("/trainee/{username}/trainings")
  @Operation(
      summary = "Get trainee's trainings",
      description = "Fetch all training sessions of a trainee")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                             [
                             {
                             "name": "testTraining",
                             "date": "2020-05-04",
                             "trainingType": {
                             "name": "HIIT",
                              "id": 5
                              },
                              "duration": 45,
                             "trainerName": "John"
                              }
                              ]
                                            """))),
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
    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content())
  })
  public ResponseEntity<List<TraineeTrainingDto>> getTraineeTrainings(
      @PathVariable("username")
          @Parameter(description = "Username of the trainee", example = "john.doe", required = true)
          String username,
      @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
          Date dateFrom,
      @RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
          Date dateTo,
      @RequestParam(value = "trainerName", required = false)
          @Parameter(description = "Trainer name of the training", example = "John")
          String trainerName,
      @RequestParam(value = "trainingType", required = false)
          @Parameter(description = "Training name", example = "HIIT")
          String trainingType) {
    var traineeTrainings =
        trainingService.getTrainingsByTraineeUsername(
            username, dateFrom, dateTo, trainerName, trainingType);

    return ResponseEntity.ok(traineeTrainings);
  }

  @RequiresAuthentication
  @PutMapping("/trainee/{username}/trainers")
  @Operation(
      summary = "Update trainee's trainers list",
      description = "Update the list of trainers assigned to a trainee",
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
            example = "b9T(ac^ydY",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "username",
            description = "Username of the trainee to update trainers list",
            example = "john.doe",
            in = ParameterIn.PATH,
            required = true)
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "List of trainers to assign to the trainee",
              content =
                  @Content(
                      mediaType = "application/json",
                      examples =
                          @ExampleObject(
                              value =
                                  """
                {
                    "trainers": [
                        {
                            "username": "trainer1",
                            "firstName": "John",
                            "lastName": "Doe",
                            "specializationId": 1
                        },
                        {
                            "username": "trainer2",
                            "firstName": "Jane",
                            "lastName": "Smith",
                            "specializationId": 2
                        }
                    ]
                }
                """))))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully updated trainers list",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                [
                    {
                        "username": "trainer1",
                        "firstName": "John",
                        "lastName": "Doe",
                        "specializationId": 1
                    },
                    {
                        "username": "trainer2",
                        "firstName": "Jane",
                        "lastName": "Smith",
                        "specializationId": 2
                    }
                ]
                """))),
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
                    "errorMessage": "Failed to update trainee's trainers list, the request contains invalid fields",
                    "errors": [
                        {
                            "field": "trainers",
                            "message": "Trainers list cannot be empty",
                            "rejectedValue": []
                        }
                    ]
                }
                """))),
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
    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content())
  })
  public ResponseEntity<List<TraineeTrainersDto>> updateTraineeTrainersList(
      @PathVariable("username") String username,
      @RequestBody @Valid UpdateTrainersDto updateTrainersDto) {
    var traineeTrainers = traineeService.updateTraineeTrainers(username, updateTrainersDto);
    return ResponseEntity.ok(traineeTrainers);
  }

  @Operation(
      summary = "Change trainee's active status",
      description = "Toggle the active status of a trainee",
      parameters = {
        @Parameter(
            name = "Username",
            description = "Trainer's username for authentication",
            example = "john.doe",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "Password",
            description = "Trainer's password for authentication",
            example = "password123",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "username",
            description = "Username of the trainer to change status",
            example = "john.doe",
            in = ParameterIn.PATH,
            required = true)
      })
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully changed trainee's status"),
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
    @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content())
  })
  @RequiresAuthentication
  @PatchMapping("/trainee/{username}/status")
  public ResponseEntity<Void> changeStatus(@PathVariable("username") String username) {
    traineeService.changeStatus(username);
    return ResponseEntity.ok().build();
  }
}
