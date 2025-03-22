package org.example.gymcrm.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TrainingTypeController {
  private final TrainingTypeService trainingTypeService;

  @GetMapping("/training-types")
  public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
    var trainingTypes = trainingTypeService.findAll();
    return ResponseEntity.ok(trainingTypes);
  }
}
