package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.dto.TraineeProfileDto;
import org.example.gymcrm.service.TraineeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TraineeController {
  private final TraineeService traineeService;

  @GetMapping("/trainees/{username}")
  public ResponseEntity<TraineeProfileDto> getTrainee(@PathVariable("username") String username) {
    var traineeProfile = traineeService.findByUsername(username);
    return ResponseEntity.ok(traineeProfile);
  }

  @PostMapping("/trainees")
  public ResponseEntity<RegisterTraineeResponseDto> registerTrainee(
      @RequestBody @Valid RegisterTraineeRequestDto trainee) {
    var registeredTrainee = traineeService.save(trainee);
    return ResponseEntity.ok(registeredTrainee);
  }
}
