package org.example.gymcrm.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TrainingTypeController.class)
@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private TrainingTypeService trainingTypeService;

  @Autowired private TrainingTypeController trainingTypeController;

  @Test
  public void getTrainingTypes() throws Exception {
    var trainingType1 = TrainingTypeDto.builder().name("CARDIO").build();
    var trainingType2 = TrainingTypeDto.builder().name("STRENGTH").build();
    var trainingTypes = List.of(trainingType1, trainingType2);

    when(trainingTypeService.findAll()).thenReturn(trainingTypes);

    mockMvc
        .perform(get("/api/v1/training-types").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value(trainingType1.getName()))
        .andExpect(jsonPath("$[1].name").value(trainingType2.getName()));

    verify(trainingTypeService, times(1)).findAll();
  }
}
