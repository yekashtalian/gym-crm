package org.example.gymcrm.web.controller;

import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TraineeService;
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

class TraineeControllerTest {

  @Mock
  private TraineeService traineeService;

  @Mock
  private TrainingService trainingService;

  @InjectMocks
  private TraineeController traineeController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getTraineeTest() {
    String username = "john";
    TraineeProfileDto traineeProfile = new TraineeProfileDto();

    when(traineeService.findByUsername(username)).thenReturn(traineeProfile);

    ResponseEntity<TraineeProfileDto> response = traineeController.getTrainee(username);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(traineeProfile, response.getBody());
    verify(traineeService).findByUsername(username);
  }

  @Test
  void registerTraineeTest() {
    RegisterTraineeRequestDto request = new RegisterTraineeRequestDto();
    RegisterTraineeResponseDto responseDto = new RegisterTraineeResponseDto();

    when(traineeService.save(request)).thenReturn(responseDto);

    ResponseEntity<RegisterTraineeResponseDto> response = traineeController.registerTrainee(request);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(responseDto, response.getBody());
    verify(traineeService).save(request);
  }

  @Test
  void updateTraineeTest() {
    String username = "john";
    UpdateTraineeRequestDto request = new UpdateTraineeRequestDto();
    TraineeProfileDto updatedProfile = new TraineeProfileDto();

    when(traineeService.update(username, request)).thenReturn(updatedProfile);

    ResponseEntity<TraineeProfileDto> response = traineeController.updateTrainee(username, request);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(updatedProfile, response.getBody());
    verify(traineeService).update(username, request);
  }

  @Test
  void deleteTraineeTest() {
    String username = "john";

    doNothing().when(traineeService).deleteByUsername(username);

    ResponseEntity<Void> response = traineeController.deleteTrainee(username);

    assertEquals(200, response.getStatusCodeValue());
    verify(traineeService).deleteByUsername(username);
  }

  @Test
  void getTraineeTrainingsTest() {
    String username = "john";
    List<TraineeTrainingDto> trainings = Collections.emptyList();

    when(trainingService.getTrainingsByTraineeUsername(eq(username), any(), any(), any(), any()))
            .thenReturn(trainings);

    ResponseEntity<List<TraineeTrainingDto>> response = traineeController.getTraineeTrainings(username, new Date(), new Date(), "trainer", "type");

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(trainings, response.getBody());
    verify(trainingService).getTrainingsByTraineeUsername(eq(username), any(), any(), any(), any());
  }

  @Test
  void updateTraineeTrainersListTest() {
    String username = "john";
    UpdateTrainersDto updateDto = new UpdateTrainersDto();
    List<TraineeTrainersDto> trainerList = Collections.emptyList();

    when(traineeService.updateTraineeTrainers(username, updateDto)).thenReturn(trainerList);

    ResponseEntity<List<TraineeTrainersDto>> response = traineeController.updateTraineeTrainersList(username, updateDto);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(trainerList, response.getBody());
    verify(traineeService).updateTraineeTrainers(username, updateDto);
  }

  @Test
  void changeStatusTest() {
    String username = "john";

    doNothing().when(traineeService).changeStatus(username);

    ResponseEntity<Void> response = traineeController.changeStatus(username);

    assertEquals(200, response.getStatusCodeValue());
    verify(traineeService).changeStatus(username);
  }
}

