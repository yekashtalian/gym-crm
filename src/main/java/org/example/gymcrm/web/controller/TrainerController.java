package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.dto.TrainerProfileDto;
import org.example.gymcrm.dto.UpdateTrainerRequestDto;
import org.example.gymcrm.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TrainerController {
  private final TrainerService trainerService;

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

  @PutMapping("/trainer/{username}")
  public ResponseEntity<TrainerProfileDto> updateTrainer(
      @PathVariable("username") String username,
      @RequestBody @Valid UpdateTrainerRequestDto trainer) {
    var trainerProfile = trainerService.update(username, trainer);
    return ResponseEntity.ok(trainerProfile);
  }

  @GetMapping("/trainers/unassigned")
  public ResponseEntity<List<TrainerProfileDto>> getUnassignedTrainers(
      @RequestParam("username") @NotEmpty(message = "Username parameter is required")
          String username) {
    var unassignedTrainersProfiles = trainerService.getUnassignedTrainers(username);
    return ResponseEntity.ok(unassignedTrainersProfiles);
  }
}
