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
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(
    name = "Trainer Controller",
    description = "Endpoints for managing trainers and their training sessions")
public class TrainerController {
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  @RequiresAuthentication
  @GetMapping("/trainer/{username}")
  @Operation(
      summary = "Get trainer by username",
      description = "Fetch trainer details by username",
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
            description = "Username of the trainer to fetch",
            example = "john.doe",
            in = ParameterIn.PATH,
            required = true)
      })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved trainer profile",
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
                            "specializationId": "3",
                            "isActive": true,
                            "trainees": [
                            ]
                        }
                        """))),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized access",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Unauthorized access"
                        }
                        """))),
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Trainer with username john.doe not found"
                        }
                        """)))
  })
  public ResponseEntity<TrainerProfileDto> getTrainer(@PathVariable("username") String username) {
    var trainerProfile = trainerService.findByUsername(username);
    return ResponseEntity.ok(trainerProfile);
  }

  @PostMapping("/trainer")
  @Operation(
      summary = "Register a new trainer",
      description = "Adds a new trainer to the system",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Trainer details to be registered",
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
                            "specializationId": "2"
                        }
                        """))))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully registered trainer",
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
                            "errorMessage": "Failed to register trainer, the request contains invalid fields",
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
  public ResponseEntity<RegisterTrainerResponseDto> registerTrainer(
      @RequestBody @Valid RegisterTrainerRequestDto trainer) {
    var registeredTrainer = trainerService.save(trainer);
    return ResponseEntity.ok(registeredTrainer);
  }

  @RequiresAuthentication
  @PutMapping("/trainer/{username}")
  @Operation(
      summary = "Update trainer details",
      description = "Update trainer information",
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
            description = "Username of the trainer to update",
            example = "john.doe",
            in = ParameterIn.PATH,
            required = true)
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Trainer details to be updated",
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
                            "specialization": "3",
                            "isActive": true
                        }
                        """))))
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully updated trainer profile",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "username": "john.doe",
                            "firstName": "John",
                            "lastName": "Doe",
                            "specialization": "3",
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
                            "errorMessage": "Failed to update trainer, the request contains invalid fields",
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
        description = "Unauthorized access",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Unauthorized access"
                        }
                        """))),
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Trainer with username john.doe not found"
                        }
                        """)))
  })
  public ResponseEntity<TrainerProfileDto> updateTrainer(
      @PathVariable("username") String username,
      @RequestBody @Valid UpdateTrainerRequestDto trainer) {
    var trainerProfile = trainerService.update(username, trainer);
    return ResponseEntity.ok(trainerProfile);
  }

  @RequiresAuthentication
  @GetMapping("/trainers/unassigned")
  @Operation(
      summary = "Get unassigned trainers",
      description = "Fetch a list of trainers who are not assigned to any trainee",
      parameters = {
        @Parameter(
            name = "Username",
            description = "Trainee's username for authentication",
            example = "john.doe",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "Password",
            description = "Trainee's password for authentication",
            example = "password123",
            in = ParameterIn.HEADER,
            required = true),
        @Parameter(
            name = "username",
            description = "Trainee's username to fetch unassigned trainers",
            example = "john.doe",
            in = ParameterIn.QUERY,
            required = true)
      })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved unassigned trainers",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        [
                            {   "username": "jane.doe",
                                "firstName": "Jane",
                                "lastName": "Doe",
                                "specializationId": "3"
                            }
                        ]
                        """))),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized access",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Unauthorized access"
                        }
                        """)))
  })
  public ResponseEntity<List<TrainerProfileDto>> getUnassignedTrainers(
      @RequestParam("username") String username) {
    var unassignedTrainersProfiles = trainerService.getUnassignedTrainers(username);
    return ResponseEntity.ok(unassignedTrainersProfiles);
  }

  @RequiresAuthentication
  @GetMapping("/trainer/{username}/trainings")
  @Operation(
      summary = "Get trainer's trainings",
      description = "Fetch all training sessions of a trainer",
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
            description = "Username of the trainer to fetch trainings",
            example = "john.doe",
            in = ParameterIn.PATH,
            required = true),
        @Parameter(
            name = "from",
            description = "Start date for filtering trainings (yyyy-MM-dd)",
            example = "2024-01-01",
            in = ParameterIn.QUERY),
        @Parameter(
            name = "to",
            description = "End date for filtering trainings (yyyy-MM-dd)",
            example = "2024-12-31",
            in = ParameterIn.QUERY),
        @Parameter(
            name = "traineeName",
            description = "Trainee's name for filtering trainings",
            example = "Jane Doe",
            in = ParameterIn.QUERY)
      })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved trainer's trainings",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        [
                            {
                                "trainingName": "Morning Cardio",
                                "trainingDate": "2024-12-01",
                                "trainingType": [
                                    {
                                        "name": "CARDIO",
                                        "id": "3"
                                    }
                                ],
                                "trainingDuration": 60,
                                "traineeName": "Jane Doe",
                            }
                        ]
                        """))),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized access",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Unauthorized access"
                        }
                        """))),
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Trainer with username john.doe not found"
                        }
                        """)))
  })
  public ResponseEntity<List<TrainerTrainingDto>> getTrainerTrainings(
      @PathVariable("username") String username,
      @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
          Date from,
      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
      @RequestParam(value = "traineeName", required = false) String traineeName) {
    var trainerTrainings =
        trainingService.getTrainingsByTrainerUsername(username, from, to, traineeName);
    return ResponseEntity.ok(trainerTrainings);
  }

  @RequiresAuthentication
  @PatchMapping("/trainer/{username}/status")
  @Operation(
      summary = "Change trainer's active status",
      description = "Toggle the active status of a trainer",
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
    @ApiResponse(responseCode = "200", description = "Successfully changed trainer's status"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized access",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Unauthorized access"
                        }
                        """))),
    @ApiResponse(
        responseCode = "404",
        description = "Trainer not found",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        {
                            "localDateTime": "2024-08-05T16:16:53.8490207",
                            "errorMessage": "Trainer with username john.doe not found"
                        }
                        """)))
  })
  public ResponseEntity<Void> changeStatus(@PathVariable("username") String username) {
    trainerService.changeStatus(username);
    return ResponseEntity.ok().build();
  }
}
