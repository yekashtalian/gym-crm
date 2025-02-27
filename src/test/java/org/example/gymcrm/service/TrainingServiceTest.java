package org.example.gymcrm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TraineeTrainingDto;
import org.example.gymcrm.dto.TrainerTrainingDto;
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.mapper.TrainingMapper;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

  @Mock private TrainingDao trainingDao;

  @Mock private TraineeDao traineeDao;

  @Mock private TrainerDao trainerDao;

  @Mock private TrainingTypeDao trainingTypeDao;

  @Mock private TrainingMapper trainingMapper;

  @InjectMocks private TrainingServiceImpl trainingService;

  private TrainingDto trainingDto;
  private Training training;
  private Trainer trainer;
  private Trainee trainee;
  private TrainingType trainingType;

  @BeforeEach
  void setUp() {
    trainer = new Trainer();
    trainee = new Trainee();
    trainingType = new TrainingType();

    trainingDto = new TrainingDto();
    trainingDto.setTrainerUsername("trainerUser");
    trainingDto.setTraineeUsername("traineeUser");

    training = new Training();
    training.setTrainer(trainer);
    training.setTrainee(trainee);
  }

  @Test
  void testSave_Success() {
    when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.of(trainer));
    when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.of(trainee));
    when(trainingMapper.toTraining(trainingDto)).thenReturn(training);

    trainingService.save(trainingDto);

    verify(trainingDao).save(training);
    verify(trainerDao).findByUsername("trainerUser");
    verify(traineeDao).findByUsername("traineeUser");
  }

  @Test
  void testSave_WhenTrainerNotFound_ThrowsException() {
    when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> trainingService.save(trainingDto))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Trainer not found");

    verify(trainerDao).findByUsername("trainerUser");
    verify(trainingDao, never()).save(any());
  }

  @Test
  void testSave_WhenTraineeNotFound_ThrowsException() {
    when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.of(trainer));
    when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> trainingService.save(trainingDto))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Trainee not found");

    verify(trainerDao).findByUsername("trainerUser");
    verify(traineeDao).findByUsername("traineeUser");
    verify(trainingDao, never()).save(any());
  }

  @Test
  void testGetTrainingsByTraineeUsername_Success() {
    Date fromDate = new Date();
    Date toDate = new Date();
    when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.of(trainee));
    when(trainingTypeDao.findByName(any())).thenReturn(Optional.of(trainingType));
    when(trainingDao.getTrainingsByTraineeUsername(
            "traineeUser", fromDate, toDate, "trainerName", trainingType))
        .thenReturn(List.of(training));
    when(trainingMapper.toTraineeTrainingsDto(training)).thenReturn(new TraineeTrainingDto());

    List<TraineeTrainingDto> result =
        trainingService.getTrainingsByTraineeUsername(
            "traineeUser", fromDate, toDate, "trainerName", "STRENGTH_TRAINING");

    assertThat(result).hasSize(1);
    verify(traineeDao).findByUsername("traineeUser");
    verify(trainingDao)
        .getTrainingsByTraineeUsername(
            "traineeUser", fromDate, toDate, "trainerName", trainingType);
  }

  @Test
  void testGetTrainingsByTraineeUsername_WhenTraineeNotFound_ThrowsException() {
    when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                trainingService.getTrainingsByTraineeUsername(
                    "traineeUser", null, null, null, null))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Trainee not found");

    verify(traineeDao).findByUsername("traineeUser");
    verify(trainingDao, never()).getTrainingsByTraineeUsername(any(), any(), any(), any(), any());
  }

  @Test
  void testGetTrainingsByTrainerUsername_Success() {
    Date fromDate = new Date();
    Date toDate = new Date();
    when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.of(trainer));
    when(trainingDao.getTrainingsByTrainerUsername("trainerUser", fromDate, toDate, "traineeName"))
        .thenReturn(List.of(training));
    when(trainingMapper.toTrainerTrainingsDto(training)).thenReturn(new TrainerTrainingDto());

    List<TrainerTrainingDto> result =
        trainingService.getTrainingsByTrainerUsername(
            "trainerUser", fromDate, toDate, "traineeName");

    assertThat(result).hasSize(1);
    verify(trainerDao).findByUsername("trainerUser");
    verify(trainingDao)
        .getTrainingsByTrainerUsername("trainerUser", fromDate, toDate, "traineeName");
  }

  @Test
  void testGetTrainingsByTrainerUsername_WhenTrainerNotFound_ThrowsException() {
    when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> trainingService.getTrainingsByTrainerUsername("trainerUser", null, null, null))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("Trainer not found");

    verify(trainerDao).findByUsername("trainerUser");
    verify(trainingDao, never()).getTrainingsByTrainerUsername(any(), any(), any(), any());
  }
}
