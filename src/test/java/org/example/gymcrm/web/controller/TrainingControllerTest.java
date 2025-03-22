package org.example.gymcrm.web.controller;

import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingControllerTest {

  @Mock
  private TrainingService trainingService;

  @InjectMocks
  private TrainingController trainingController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void saveTrainingTest() {
    TrainingDto trainingDto =
            TrainingDto.builder()
                       .traineeUsername("trainee.username")
                       .trainerUsername("trainer.username")
                       .trainingName("Strength")
                       .trainingDate(new Date())
                       .duration(60)
                       .build();

    doNothing().when(trainingService).save(trainingDto);

    ResponseEntity<Void> response = trainingController.saveTraining(trainingDto);

    assertEquals(200, response.getStatusCodeValue());
    verify(trainingService).save(trainingDto);
  }
}
