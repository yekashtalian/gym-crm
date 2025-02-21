package org.example.gymcrm.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TrainingControllerTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private TrainingService trainingService;

  @InjectMocks private TrainingController trainingController;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void saveTraining() throws Exception {
    var trainingDto =
        TrainingDto.builder()
            .traineeUsername("trainee.username")
            .trainerUsername("trainer.username")
            .trainingName("Yoga")
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
