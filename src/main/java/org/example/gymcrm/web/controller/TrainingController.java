package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.aspect.RequiresAuthentication;
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class TrainingController {
  private final TrainingService trainingService;

  @RequiresAuthentication
  @PostMapping("/training")
  public ResponseEntity<Void> saveTraining(@RequestBody @Valid TrainingDto training) {
    trainingService.save(training);
    return ResponseEntity.ok().build();
  }
}
