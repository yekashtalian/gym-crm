package org.example.gymcrm.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TrainerService;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TrainerControllerTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock private TrainerService trainerService;

  @Mock private TrainingService trainingService;

  @InjectMocks private TrainerController trainerController;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void getTrainer() throws Exception {
    var username = "trainer.username";
    var trainerProfile =
        TrainerProfileDto.builder()
            .firstName("Trainer")
            .lastName("Name")
            .specialization(1L)
            .active(true)
            .build();

    when(trainerService.findByUsername(username)).thenReturn(trainerProfile);

    mockMvc
        .perform(get("/api/v1/trainer/{username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(trainerProfile.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(trainerProfile.getLastName()))
        .andExpect(jsonPath("$.specialization").value(trainerProfile.getSpecialization()))
        .andExpect(jsonPath("$.active").value(trainerProfile.getActive()));
  }

  @Test
  public void registerTrainer() throws Exception {
    var requestDto =
        RegisterTrainerRequestDto.builder()
            .firstName("John")
            .lastName("Doe")
            .specializationId(1L)
            .build();
    var requestJson = objectMapper.writeValueAsString(requestDto);
    var responseDto =
        RegisterTrainerResponseDto.builder().username("john.doe").password("12345").build();

    when(trainerService.save(any(RegisterTrainerRequestDto.class))).thenReturn(responseDto);
    mockMvc
        .perform(
            post("/api/v1/trainer").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
        .andExpect(jsonPath("$.password").value(responseDto.getPassword()));

    verify(trainerService, times(1)).save(any());
  }

  @Test
  public void updateTrainer() throws Exception {
    var username = "john.doe";
    var updateDto =
        UpdateTrainerRequestDto.builder()
            .username(username)
            .firstName("John")
            .lastName("Doe")
            .specializationId(1L)
            .active(false)
            .build();
    var updateJson = objectMapper.writeValueAsString(updateDto);

    var trainerProfile =
        TrainerProfileDto.builder()
            .username(username)
            .firstName("John")
            .lastName("Doe")
            .specialization(1L)
            .active(false)
            .build();

    when(trainerService.update(eq(username), any())).thenReturn(trainerProfile);

    mockMvc
        .perform(
            put("/api/v1/trainer/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(trainerProfile.getUsername()))
        .andExpect(jsonPath("$.firstName").value(trainerProfile.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(trainerProfile.getLastName()))
        .andExpect(jsonPath("$.specialization").value(trainerProfile.getSpecialization()))
        .andExpect(jsonPath("$.active").value(trainerProfile.getActive()));

    verify(trainerService, times(1)).update(eq(username), any());
  }

  @Test
  public void getUnassignedTrainers() throws Exception {
    var username = "admin.user";
    var trainer1 =
        TrainerProfileDto.builder()
            .username("trainer1.one")
            .firstName("Trainer1")
            .lastName("One")
            .specialization(1L)
            .active(true)
            .build();
    var trainer2 =
        TrainerProfileDto.builder()
            .username("trainer2.two")
            .firstName("Trainer2")
            .lastName("Two")
            .specialization(1L)
            .active(false)
            .build();
    var unassignedTrainers = List.of(trainer1, trainer2);

    when(trainerService.getUnassignedTrainers(username)).thenReturn(unassignedTrainers);

    mockMvc
        .perform(get("/api/v1/trainers/unassigned?username={username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$[0].username").value(trainer1.getUsername()))
        .andExpect(jsonPath("$[0].firstName").value(trainer1.getFirstName()))
        .andExpect(jsonPath("$[0].lastName").value(trainer1.getLastName()))
        .andExpect(jsonPath("$[0].specialization").value(trainer1.getSpecialization()))
        .andExpect(jsonPath("$[0].active").value(trainer1.getActive()))
        .andExpect(jsonPath("$[1].username").value(trainer2.getUsername()))
        .andExpect(jsonPath("$[1].firstName").value(trainer2.getFirstName()))
        .andExpect(jsonPath("$[1].lastName").value(trainer2.getLastName()))
        .andExpect(jsonPath("$[1].specialization").value(trainer2.getSpecialization()))
        .andExpect(jsonPath("$[1].active").value(trainer2.getActive()));
  }

  @Test
  public void getTrainerTrainings() throws Exception {
    var username = "john.doe";
    var trainerTraining1 =
        TrainerTrainingDto.builder()
            .name("Yoga Session")
            .date(new Date(1000))
            .duration(60)
            .traineeName("Trainee1")
            .build();
    var trainerTraining2 =
        TrainerTrainingDto.builder()
            .name("Pilates Class")
            .date(new Date(2000))
            .duration(45)
            .traineeName("Trainee2")
            .build();
    var trainingList = List.of(trainerTraining1, trainerTraining2);

    when(trainingService.getTrainingsByTrainerUsername(eq(username), any(), any(), any()))
        .thenReturn(trainingList);

    mockMvc
        .perform(get("/api/v1/trainer/{username}/trainings", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$[0].name").value(trainerTraining1.getName()))
        .andExpect(jsonPath("$[0].traineeName").value(trainerTraining1.getTraineeName()))
        .andExpect(jsonPath("$[1].name").value(trainerTraining2.getName()))
        .andExpect(jsonPath("$[1].traineeName").value(trainerTraining2.getTraineeName()));
  }

  @Test
  public void changeStatus() throws Exception {
    var username = "john.doe";
    doNothing().when(trainerService).changeStatus(username);

    mockMvc
        .perform(patch("/api/v1/trainer/{username}/status", username))
        .andExpect(status().isOk());
  }
}
