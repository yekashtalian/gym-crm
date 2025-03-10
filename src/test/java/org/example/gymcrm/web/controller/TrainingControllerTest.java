package org.example.gymcrm.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TrainingController.class)
@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private TrainingService trainingService;

  @Autowired private TrainingController trainingController;

  @Test
  public void saveTraining() throws Exception {
    var trainingDto =
        TrainingDto.builder()
            .traineeUsername("trainee.username")
            .trainerUsername("trainer.username")
            .trainingName("Strength")
            .trainingDate(new Date(1000))
            .duration(60)
            .build();
    var trainingJson = objectMapper.writeValueAsString(trainingDto);

    mockMvc
        .perform(
            post("/api/v1/training").contentType(MediaType.APPLICATION_JSON).content(trainingJson))
        .andExpect(status().isOk());

    verify(trainingService, times(1)).save(any(TrainingDto.class));
  }
}
