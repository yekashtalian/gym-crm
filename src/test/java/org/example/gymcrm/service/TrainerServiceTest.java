package org.example.gymcrm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.mapper.TrainerMapper;
import org.example.gymcrm.security.service.JwtService;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

  @Mock private TrainerDao trainerDao;
  @Mock private TraineeDao traineeDao;
  @Mock private TrainingTypeDao trainingTypeDao;
  @Mock private TrainerMapper trainerMapper;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtService jwtService;

  @InjectMocks private TrainerServiceImpl trainerService;

  private Trainer trainer;
  private User user;
  private TrainingType trainingType;
  private RegisterTrainerRequestDto registerTrainerRequestDto;
  private RegisterTrainerResponseDto registerTrainerResponseDto;
  private UpdateTrainerRequestDto updateTrainerRequestDto;
  private TrainerProfileDto trainerProfileDto;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setUsername("john.doe");
    user.setPassword("password");
    user.setActive(true);

    trainingType = new TrainingType();
    trainingType.setId(1L);
    trainingType.setName(TrainingType.Type.CARDIO);

    trainer = new Trainer();
    trainer.setUser(user);
    trainer.setSpecialization(trainingType);

    registerTrainerRequestDto = new RegisterTrainerRequestDto();
    registerTrainerRequestDto.setFirstName("John");
    registerTrainerRequestDto.setLastName("Doe");
    registerTrainerRequestDto.setSpecializationId(1L);

    registerTrainerResponseDto = new RegisterTrainerResponseDto();
    registerTrainerResponseDto.setUsername("john.doe");

    updateTrainerRequestDto = new UpdateTrainerRequestDto();
    updateTrainerRequestDto.setFirstName("Jane");
    updateTrainerRequestDto.setLastName("Doe");
    updateTrainerRequestDto.setActive(false);
    updateTrainerRequestDto.setSpecializationId(1L);

    trainerProfileDto = new TrainerProfileDto();
    trainerProfileDto.setUsername("john.doe");
    trainerProfileDto.setFirstName("John");
    trainerProfileDto.setLastName("Doe");
    trainerProfileDto.setSpecialization(1L);
    trainerProfileDto.setActive(true);
  }

  @Test
  void save_ShouldRegisterTrainer_WhenSpecializationExists() {
    when(trainerMapper.registerDtoToUser(registerTrainerRequestDto)).thenReturn(user);
    when(trainingTypeDao.findById(1L)).thenReturn(Optional.of(trainingType));
    when(traineeDao.findUsernames()).thenReturn(Collections.emptyList());
    when(trainerDao.findUsernames()).thenReturn(Collections.emptyList());
    when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(trainerMapper.trainerToDto(any(User.class))).thenReturn(registerTrainerResponseDto);
    when(jwtService.generateToken(any(User.class))).thenReturn("mockToken");

    RegisterTrainerResponseDto response = trainerService.save(registerTrainerRequestDto);

    assertNotNull(response);
    assertEquals("john.doe", response.getUsername());
    assertEquals("mockToken", response.getToken());

    verify(trainerDao).save(any(Trainer.class));
    verify(trainerMapper).trainerToDto(any(User.class));
    verify(trainingTypeDao).findById(1L);
    verify(jwtService).generateToken(any(User.class));
  }

  @Test
  void update_ShouldUpdateTrainer_WhenTrainerExists() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
    when(trainingTypeDao.findById(1L)).thenReturn(Optional.of(trainingType));
    when(trainerDao.update(trainer)).thenReturn(trainer);
    when(trainerMapper.toProfileDto(trainer)).thenReturn(trainerProfileDto);

    TrainerProfileDto response = trainerService.update("john.doe", updateTrainerRequestDto);

    assertNotNull(response);
    assertEquals("john.doe", response.getUsername());
    verify(trainerDao).update(trainer);
  }

  @Test
  void update_ShouldThrowException_WhenTrainerNotFound() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> trainerService.update("john.doe", updateTrainerRequestDto));

    verify(trainerDao).findByUsername("john.doe");
    verifyNoInteractions(trainingTypeDao);
  }

  @Test
  void findByUsername_ShouldReturnTrainerProfile_WhenTrainerExists() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
    when(trainerMapper.toProfileDto(trainer)).thenReturn(trainerProfileDto);

    TrainerProfileDto response = trainerService.findByUsername("john.doe");

    assertNotNull(response);
    assertEquals("john.doe", response.getUsername());
  }

  @Test
  void findByUsername_ShouldThrowException_WhenTrainerNotFound() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> trainerService.findByUsername("john.doe"));

    verify(trainerDao).findByUsername("john.doe");
  }

  @Test
  void changeStatus_ShouldToggleTrainerStatus_WhenTrainerExists() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    trainerService.changeStatus("john.doe");

    assertFalse(trainer.getUser().isActive());
    verify(trainerDao).findByUsername("john.doe");
  }

  @Test
  void changeStatus_ShouldThrowException_WhenTrainerNotFound() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> trainerService.changeStatus("john.doe"));

    verify(trainerDao).findByUsername("john.doe");
  }

  @Test
  void getUnassignedTrainers_ShouldReturnTrainers_WhenTraineeExists() {
    when(traineeDao.findByUsername("trainee.username"))
        .thenReturn(Optional.of(new org.example.gymcrm.entity.Trainee()));
    when(trainerDao.findUnassignedTrainersByTraineeUsername("trainee.username"))
        .thenReturn(Collections.singletonList(trainer));
    when(trainerMapper.toProfileDtoForUnassigned(trainer)).thenReturn(trainerProfileDto);

    List<TrainerProfileDto> response = trainerService.getUnassignedTrainers("trainee.username");

    assertNotNull(response);
    assertEquals(1, response.size());
    assertEquals("john.doe", response.get(0).getUsername());
  }

  @Test
  void getUnassignedTrainers_ShouldThrowException_WhenTraineeNotFound() {
    when(traineeDao.findByUsername("trainee.username")).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> trainerService.getUnassignedTrainers("trainee.username"));

    verify(traineeDao).findByUsername("trainee.username");
  }
}
