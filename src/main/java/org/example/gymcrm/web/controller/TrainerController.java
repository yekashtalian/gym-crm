package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class TrainerController {
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  @GetMapping("/trainer/{username}")
  public ResponseEntity<TrainerProfileDto> getTrainer(
      @PathVariable("username")
          @NotEmpty(message = "Username cannot be empty")
          @Pattern(
              regexp = "^[A-Za-z]+\\.[A-Za-z]+$",
              message =
                  "Invalid format. Use two English words separated by a dot (e.g., john.doe).")
          String username) {
    var trainerProfile = trainerService.findByUsername(username);
    return ResponseEntity.ok(trainerProfile);
  }

  @PostMapping("/trainer")
  public ResponseEntity<RegisterTrainerResponseDto> registerTrainer(
      @RequestBody @Valid RegisterTrainerRequestDto trainer) {
    var registeredTrainer = trainerService.save(trainer);
    return ResponseEntity.ok(registeredTrainer);
  }

  @PutMapping("/trainer/{username}")
  public ResponseEntity<TrainerProfileDto> updateTrainer(
      @PathVariable("username") String username,
      @RequestBody @Valid UpdateTrainerRequestDto trainer) {
    var trainerProfile = trainerService.update(username, trainer);
    return ResponseEntity.ok(trainerProfile);
  }

  @GetMapping("/trainers/unassigned")
  public ResponseEntity<List<TrainerProfileDto>> getUnassignedTrainers(
      @RequestParam("username") @NotEmpty(message = "Username cannot be empty") String username) {
    var unassignedTrainersProfiles = trainerService.getUnassignedTrainers(username);
    return ResponseEntity.ok(unassignedTrainersProfiles);
  }

  @GetMapping("/trainer/{username}/trainings")
  public ResponseEntity<List<TrainerTrainingDto>> getTrainerTrainings(
      @PathVariable("username")
          @NotEmpty(message = "Username cannot be empty")
          @Pattern(
              regexp = "^[A-Za-z]+\\.[A-Za-z]+$",
              message =
                  "Invalid format. Use two English words separated by a dot (e.g., john.doe).")
          String username,
      @RequestParam(value = "from", required = false) Date from,
      @RequestParam(value = "to", required = false) Date to,
      @RequestParam(value = "traineeName", required = false) String traineeName) {
    var trainerTrainings =
        trainingService.getTrainingsByTrainerUsername(username, from, to, traineeName);
    return ResponseEntity.ok(trainerTrainings);
  }

  @PatchMapping("/trainer/{username}/status")
  public ResponseEntity<Void> changeStatus(
      @PathVariable("username")
          @NotEmpty(message = "Username cannot be empty")
          @Pattern(
              regexp = "^[A-Za-z]+\\.[A-Za-z]+$",
              message =
                  "Invalid format. Use two English words separated by a dot (e.g., john.doe).")
          String username) {
    trainerService.changeStatus(username);
    return ResponseEntity.ok().build();
  }
}
