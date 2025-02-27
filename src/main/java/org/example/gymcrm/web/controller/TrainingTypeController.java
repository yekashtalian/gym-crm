package org.example.gymcrm.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Training Type Controller", description = "Endpoints for managing training types")
public class TrainingTypeController {
  private final TrainingTypeService trainingTypeService;

  @RequiresAuthentication
  @GetMapping("/training-types")
  @Operation(
      summary = "Get all training types",
      description = "Fetch a list of all available training types",
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
      })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved training types",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                        [
                            {
                                "id": 1,
                                "name": "Cardio"
                            },
                            {
                                "id": 2,
                                "name": "Strength Training"
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
                }))
  })
  public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
    var trainingTypes = trainingTypeService.findAll();
    return ResponseEntity.ok(trainingTypes);
  }
}
