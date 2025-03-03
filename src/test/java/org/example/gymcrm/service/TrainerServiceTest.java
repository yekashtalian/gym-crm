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
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.mapper.TrainerMapper;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

  @Mock private TrainerDao trainerDao;

  @Mock private TraineeDao traineeDao;

  @Mock private TrainingTypeDao trainingTypeDao;

  @Mock private TrainerMapper trainerMapper;

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

    trainerProfileDto = new TrainerProfileDto();
    trainerProfileDto.setUsername("john.doe");
    trainerProfileDto.setFirstName("John");
    trainerProfileDto.setLastName("Doe");
    trainerProfileDto.setSpecialization(1L);
    trainerProfileDto.setActive(true);
  }

  @Test
  void testSave() {
    when(trainerMapper.registerDtoToUser(registerTrainerRequestDto)).thenReturn(user);
    when(trainingTypeDao.findById(1L)).thenReturn(Optional.of(trainingType));
    when(traineeDao.findUsernames()).thenReturn(Collections.emptyList());
    when(trainerDao.findUsernames()).thenReturn(Collections.emptyList());
    when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(trainerMapper.trainerToDto(any(User.class))).thenReturn(registerTrainerResponseDto);

    RegisterTrainerResponseDto response = trainerService.save(registerTrainerRequestDto);

    assertNotNull(response);
    assertEquals("john.doe", response.getUsername());

    verify(trainerDao, times(1)).save(any(Trainer.class));
    verify(trainerMapper, times(1)).trainerToDto(any(User.class));
    verify(trainingTypeDao, times(1)).findById(1L);
    verify(traineeDao, times(1)).findUsernames();
    verify(trainerDao, times(1)).findUsernames();
  }


  @Test
  void testSave_SpecializationNotFound() {
    when(trainerMapper.registerDtoToUser(registerTrainerRequestDto)).thenReturn(user);
    when(trainingTypeDao.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        TrainerServiceException.class, () -> trainerService.save(registerTrainerRequestDto));
  }

  @Test
  void testUpdate() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
    when(trainerDao.update(trainer)).thenReturn(trainer);
    when(trainerMapper.toProfileDto(trainer)).thenReturn(trainerProfileDto);

    TrainerProfileDto response = trainerService.update("john.doe", updateTrainerRequestDto);

    assertNotNull(response);
    assertEquals("john.doe", response.getUsername());
    verify(trainerDao, times(1)).update(trainer);
  }

  @Test
  void testUpdate_TrainerNotFound() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> trainerService.update("john.doe", updateTrainerRequestDto));
  }

  @Test
  void testFindByUsername() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
    when(trainerMapper.toProfileDto(trainer)).thenReturn(trainerProfileDto);

    TrainerProfileDto response = trainerService.findByUsername("john.doe");

    assertNotNull(response);
    assertEquals("john.doe", response.getUsername());
  }

  @Test
  void testFindByUsername_TrainerNotFound() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> trainerService.findByUsername("john.doe"));
  }

  @Test
  void testChangeStatus() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    trainerService.changeStatus("john.doe");

    assertFalse(trainer.getUser().isActive());
    verify(trainerDao, times(1)).findByUsername("john.doe");
  }

  @Test
  void testChangeStatus_TrainerNotFound() {
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> trainerService.changeStatus("john.doe"));
  }

  @Test
  void testGetUnassignedTrainers() {
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
  void testGetUnassignedTrainers_TraineeNotFound() {
    when(traineeDao.findByUsername("trainee.username")).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> trainerService.getUnassignedTrainers("trainee.username"));
  }
}
