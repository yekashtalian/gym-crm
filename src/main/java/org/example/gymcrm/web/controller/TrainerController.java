package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TrainerController {
  private final TrainerService trainerService;

  @PostMapping("/trainers")
  public ResponseEntity<RegisterTrainerResponseDto> registerTrainer(
      @RequestBody @Valid RegisterTrainerRequestDto trainer) {
    var registeredTrainer = trainerService.save(trainer);
    return ResponseEntity.ok(registeredTrainer);
  }
}
