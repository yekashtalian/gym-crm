package org.example.gymcrm.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeAll;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TraineeControllerTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  @Mock private TraineeService traineeService;
  @Mock private TrainingService trainingService;
  @InjectMocks private TraineeController traineeController;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void getTrainee() throws Exception {
    var username = "name.surname";
    var traineeProfile =
        TraineeProfileDto.builder()
            .firstName("Name")
            .lastName("Surname")
            .dateOfBirth(new Date(1000))
            .active(false)
            .address("Test address")
            .trainers(List.of())
            .build();

    when(traineeService.findByUsername(username)).thenReturn(traineeProfile);

    mockMvc
        .perform(get("/api/v1/trainee/{username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(traineeProfile.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(traineeProfile.getLastName()))
        .andExpect(jsonPath("$.dateOfBirth").exists())
        .andExpect(jsonPath("$.active").value(traineeProfile.isActive()))
        .andExpect(jsonPath("$.address").value(traineeProfile.getAddress()));
  }

  @Test
  public void registerTrainee() throws Exception {
    var requestDto =
        RegisterTraineeRequestDto.builder()
            .firstName("John")
            .lastName("Doe")
            .dateOfBirth(new Date(1000))
            .address("Test address")
            .build();
    var requestJson = objectMapper.writeValueAsString(requestDto);
    var responseDto =
        RegisterTraineeResponseDto.builder().username("john.doe").password("12345").build();

    when(traineeService.save(any(RegisterTraineeRequestDto.class))).thenReturn(responseDto);
    mockMvc
        .perform(
            post("/api/v1/trainee").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(responseDto.getUsername()));

    verify(traineeService, times(1)).save(any());
  }

  @Test
  public void updateTrainee() throws Exception {
    var username = "john.doe";
    var updateDto =
        UpdateTraineeRequestDto.builder()
            .firstName("John")
            .lastName("Doe")
            .address("Test address")
            .active(false)
            .build();
    var updateJson = objectMapper.writeValueAsString(updateDto);

    var traineeProfile =
        TraineeProfileDto.builder()
            .username(username)
            .firstName("John")
            .lastName("Doe")
            .active(false)
            .address("Test address")
            .trainers(List.of())
            .build();

    when(traineeService.update(eq(username), any())).thenReturn(traineeProfile);

    mockMvc
        .perform(
            put("/api/v1/trainee/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(traineeProfile.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(traineeProfile.getLastName()))
        .andExpect(jsonPath("$.active").value(traineeProfile.isActive()))
        .andExpect(jsonPath("$.address").value(traineeProfile.getAddress()));

    verify(traineeService, times(1)).update(eq(username), any());
  }

  @Test
  public void deleteTrainee() throws Exception {
    var username = "john.doe";

    doNothing().when(traineeService).deleteByUsername(username);

    mockMvc.perform(delete("/api/v1/trainee/{username}", username)).andExpect(status().isOk());

    verify(traineeService, times(1)).deleteByUsername(username);
  }

  @Test
  public void getTraineeTrainings() throws Exception {
    var username = "john.doe";
    var trainingType = TrainingTypeDto.builder().name("CARDIO").build();
    var traineeTraining1 =
        TraineeTrainingDto.builder()
            .name("Test name1")
            .date(new Date(1000))
            .trainingType(trainingType)
            .duration(35)
            .trainerName("Trainer1")
            .build();
    var traineeTraining2 =
        TraineeTrainingDto.builder()
            .name("Test name2")
            .date(new Date(1000))
            .trainingType(trainingType)
            .duration(45)
            .trainerName("Trainer2")
            .build();
    var trainingList = List.of(traineeTraining1, traineeTraining2);

    when(trainingService.getTrainingsByTraineeUsername(eq(username), any(), any(), any(), any()))
        .thenReturn(trainingList);

    mockMvc
        .perform(get("/api/v1/trainee/{username}/trainings", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$[0].name").value(traineeTraining1.getName()))
        .andExpect(
            jsonPath("$[0].trainingType.name").value(traineeTraining1.getTrainingType().getName()))
        .andExpect(jsonPath("$[0].trainerName").value(traineeTraining1.getTrainerName()))
        .andExpect(jsonPath("$[1].name").value(traineeTraining2.getName()))
        .andExpect(
            jsonPath("$[1].trainingType.name").value(traineeTraining2.getTrainingType().getName()))
        .andExpect(jsonPath("$[1].trainerName").value(traineeTraining2.getTrainerName()));
  }

  @Test
  public void changeStatus() throws Exception {
    var username = "john.doe";
    doNothing().when(traineeService).changeStatus(username);

    mockMvc
        .perform(patch("/api/v1/trainee/{username}/status", username))
        .andExpect(status().isOk());
  }

  @Test
  void updateTraineeTrainersList_ShouldReturnUpdatedTrainers() throws Exception {
    String username = "testuser";
    UpdateTrainersDto updateTrainersDto = new UpdateTrainersDto(List.of("trainer1", "trainer2"));

    var trainer1 = TraineeTrainersDto.builder().username("trainer1").build();
    var trainer2 = TraineeTrainersDto.builder().username("trainer2").build();
    List<TraineeTrainersDto> responseDto = List.of(trainer1, trainer2);

    when(traineeService.updateTraineeTrainers(eq(username), any(UpdateTrainersDto.class)))
            .thenReturn(responseDto);

    mockMvc
            .perform(
                    put("/api/v1/trainee/{username}/trainers", username)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTrainersDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].username").value("trainer1"))
            .andExpect(jsonPath("$[1].username").value("trainer2"));

    verify(traineeService).updateTraineeTrainers(eq(username), any(UpdateTrainersDto.class));
  }

  @Test
  void updateTraineeTrainersList_ShouldReturnBadRequestWhenInvalid() throws Exception {
    String username = "testuser";
    UpdateTrainersDto invalidDto = new UpdateTrainersDto(null);

    mockMvc
        .perform(
            put("/api/v1/trainee/{username}/trainers", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(traineeService);
  }
}
