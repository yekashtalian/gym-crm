package org.example.gymcrm.web.controller;

import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
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
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @GetMapping("/trainee/{username}")
    public ResponseEntity<TraineeProfileDto> getTrainee(@PathVariable("username") String username) {
        var traineeProfile = traineeService.findByUsername(username);
        return ResponseEntity.ok(traineeProfile);
    }

    @PostMapping("/trainee")
    public ResponseEntity<RegisterTraineeResponseDto> registerTrainee(
            @RequestBody @Valid RegisterTraineeRequestDto trainee) {
        var registeredTrainee = traineeService.save(trainee);
        return ResponseEntity.ok(registeredTrainee);
    }

    @PutMapping("/trainee/{username}")
    public ResponseEntity<TraineeProfileDto> updateTrainee(
            @PathVariable("username") String username,
            @RequestBody @Valid UpdateTraineeRequestDto trainee) {
        var traineeProfile = traineeService.update(username, trainee);
        return ResponseEntity.ok(traineeProfile);
    }

    @DeleteMapping("/trainee/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("username") String username) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("/trainee/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingDto>> getTraineeTrainings(
            @PathVariable("username") String username,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date dateTo,
            @RequestParam(value = "trainerName", required = false) String trainerName,
            @RequestParam(value = "trainingType", required = false) String trainingType) {
        var traineeTrainings =
                trainingService.getTrainingsByTraineeUsername(
                        username, dateFrom, dateTo, trainerName, trainingType);
        return ResponseEntity.ok(traineeTrainings);
    }

    @PutMapping("/trainee/{username}/trainers")
    public ResponseEntity<List<TraineeTrainersDto>> updateTraineeTrainersList(
            @PathVariable("username") String username,
            @RequestBody @Valid UpdateTrainersDto updateTrainersDto) {
        var traineeTrainers = traineeService.updateTraineeTrainers(username, updateTrainersDto);
        return ResponseEntity.ok(traineeTrainers);
    }

    @PatchMapping("/trainee/{username}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable("username") String username) {
        traineeService.changeStatus(username);
        return ResponseEntity.ok()
                             .build();
    }
}
