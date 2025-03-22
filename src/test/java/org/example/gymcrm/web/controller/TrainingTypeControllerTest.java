package org.example.gymcrm.web.controller;

import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingTypeControllerTest {

  @Mock
  private TrainingTypeService trainingTypeService;

  @InjectMocks
  private TrainingTypeController trainingTypeController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getTrainingTypesTest() {
    TrainingTypeDto trainingType1 = TrainingTypeDto.builder().name("CARDIO").build();
    TrainingTypeDto trainingType2 = TrainingTypeDto.builder().name("STRENGTH").build();
    List<TrainingTypeDto> trainingTypes = List.of(trainingType1, trainingType2);

    when(trainingTypeService.findAll()).thenReturn(trainingTypes);

    ResponseEntity<List<TrainingTypeDto>> response = trainingTypeController.getTrainingTypes();

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(trainingTypes, response.getBody());
    verify(trainingTypeService).findAll();
  }
}
