package org.example.gymcrm.web.controller;

import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

  @Mock
  private TrainerService trainerService;

  @Mock
  private TrainingService trainingService;

  @InjectMocks
  private TrainerController trainerController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getTrainerTest() {
    String username = "trainer.username";
    TrainerProfileDto trainerProfile = new TrainerProfileDto();

    when(trainerService.findByUsername(username)).thenReturn(trainerProfile);

    ResponseEntity<TrainerProfileDto> response = trainerController.getTrainer(username);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(trainerProfile, response.getBody());
    verify(trainerService).findByUsername(username);
  }

  @Test
  void registerTrainerTest() {
    RegisterTrainerRequestDto request = new RegisterTrainerRequestDto();
    RegisterTrainerResponseDto responseDto = new RegisterTrainerResponseDto();

    when(trainerService.save(request)).thenReturn(responseDto);

    ResponseEntity<RegisterTrainerResponseDto> response = trainerController.registerTrainer(request);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(responseDto, response.getBody());
    verify(trainerService).save(request);
  }

  @Test
  void updateTrainerTest() {
    String username = "john.doe";
    UpdateTrainerRequestDto request = new UpdateTrainerRequestDto();
    TrainerProfileDto updatedProfile = new TrainerProfileDto();

    when(trainerService.update(username, request)).thenReturn(updatedProfile);

    ResponseEntity<TrainerProfileDto> response = trainerController.updateTrainer(username, request);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(updatedProfile, response.getBody());
    verify(trainerService).update(username, request);
  }

  @Test
  void getUnassignedTrainersTest() {
    String username = "admin.user";
    List<TrainerProfileDto> unassignedTrainers = Collections.emptyList();

    when(trainerService.getUnassignedTrainers(username)).thenReturn(unassignedTrainers);

    ResponseEntity<List<TrainerProfileDto>> response = trainerController.getUnassignedTrainers(username);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(unassignedTrainers, response.getBody());
    verify(trainerService).getUnassignedTrainers(username);
  }

  @Test
  void getTrainerTrainingsTest() {
    String username = "john.doe";
    List<TrainerTrainingDto> trainings = Collections.emptyList();

    when(trainingService.getTrainingsByTrainerUsername(eq(username), any(), any(), any())).thenReturn(trainings);

    ResponseEntity<List<TrainerTrainingDto>> response = trainerController.getTrainerTrainings(username, new Date(), new Date(), "traineeName");

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(trainings, response.getBody());
    verify(trainingService).getTrainingsByTrainerUsername(eq(username), any(), any(), any());
  }

  @Test
  void changeStatusTest() {
    String username = "john.doe";

    doNothing().when(trainerService).changeStatus(username);

    ResponseEntity<Void> response = trainerController.changeStatus(username);

    assertEquals(200, response.getStatusCodeValue());
    verify(trainerService).changeStatus(username);
  }
}
