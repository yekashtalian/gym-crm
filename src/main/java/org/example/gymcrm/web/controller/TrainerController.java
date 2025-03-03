package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class TrainerController {
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  @RequiresAuthentication
  @GetMapping("/trainer/{username}")
  public ResponseEntity<TrainerProfileDto> getTrainer(@PathVariable("username") String username) {
    var trainerProfile = trainerService.findByUsername(username);
    return ResponseEntity.ok(trainerProfile);
  }

  @PostMapping("/trainer")
  public ResponseEntity<RegisterTrainerResponseDto> registerTrainer(
      @RequestBody @Valid RegisterTrainerRequestDto trainer) {
    var registeredTrainer = trainerService.save(trainer);
    return ResponseEntity.ok(registeredTrainer);
  }

  @RequiresAuthentication
  @PutMapping("/trainer/{username}")
  public ResponseEntity<TrainerProfileDto> updateTrainer(
      @PathVariable("username") String username,
      @RequestBody @Valid UpdateTrainerRequestDto trainer) {
    var trainerProfile = trainerService.update(username, trainer);
    return ResponseEntity.ok(trainerProfile);
  }

  @RequiresAuthentication
  @GetMapping("/trainers/unassigned")
  public ResponseEntity<List<TrainerProfileDto>> getUnassignedTrainers(
      @RequestParam("username") String username) {
    var unassignedTrainersProfiles = trainerService.getUnassignedTrainers(username);
    return ResponseEntity.ok(unassignedTrainersProfiles);
  }

  @RequiresAuthentication
  @GetMapping("/trainer/{username}/trainings")
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
  public ResponseEntity<Void> changeStatus(@PathVariable("username") String username) {
    trainerService.changeStatus(username);
    return ResponseEntity.ok().build();
  }
}
