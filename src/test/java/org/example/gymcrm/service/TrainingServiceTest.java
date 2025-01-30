package org.example.gymcrm.service;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.enums.TrainingType;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
  @Mock private TrainingDao trainingDao;
  @Mock private TraineeDao traineeDao;
  @Mock private TrainerDao trainerDao;
  @InjectMocks private TrainingService trainingService = new TrainingServiceImpl();

  @Test
  void findAllTrainings() {
    List<Training> expectedTrainings =
        List.of(
            new Training(
                "123", "456", "training1", TrainingType.INDIVIDUAL, LocalDate.of(2025, 1, 25), 60),
            new Training(
                "123",
                "456",
                "training2",
                TrainingType.STRENGTH_TRAINING,
                LocalDate.of(2025, 1, 29),
                45),
            new Training(
                "742", "555", "training3", TrainingType.CARDIO, LocalDate.of(2025, 1, 20), 30));
    when(trainingDao.findAll()).thenReturn(expectedTrainings);

    var actualTrainings = trainingService.getAll();

    assertThat(actualTrainings).hasSameSizeAs(expectedTrainings);
    assertAll(
        () -> assertThat(actualTrainings.get(0).getTrainingName()).isEqualTo("training1"),
        () -> assertThat(actualTrainings.get(1).getTrainingName()).isEqualTo("training2"),
        () -> assertThat(actualTrainings.get(2).getTrainingName()).isEqualTo("training3"));

    verify(trainingDao, times(1)).findAll();
  }

  @Test
  void findAllTrainingsIfListIsEmpty() {
    List<Training> expectedTrainings = List.of();
    when(trainingDao.findAll()).thenReturn(expectedTrainings);

    var actualTrainings = trainingService.getAll();

    assertThat(actualTrainings).isNotNull();
    assertThat(actualTrainings).isEmpty();
    assertThat(actualTrainings).hasSameSizeAs(expectedTrainings);

    verify(trainingDao, times(1)).findAll();
  }

  @Test
  void saveTraining() {
    var training =
        new Training(
            "123", "456", "training1", TrainingType.INDIVIDUAL, LocalDate.of(2025, 1, 25), 60);
    var trainer =
        new Trainer(
            "TestName", "TestSurname", "testUsername", "password", true, TrainingType.BOXING);
    trainer.setUserId(training.getTrainerId());
    var trainee =
        new Trainee(
            "TestName",
            "TestSurname",
            "testusername",
            "testpassword",
            true,
            LocalDate.of(2000, 2, 20),
            "Test Address");
    trainee.setUserId(training.getTraineeId());

    when(traineeDao.findById(training.getTraineeId())).thenReturn(Optional.of(trainee));
    when(trainerDao.findById(training.getTrainerId())).thenReturn(Optional.of(trainer));
    doNothing().when(trainingDao).save(training);

    trainingService.save(training);

    verify(traineeDao, times(1)).findById(trainee.getUserId());
    verify(trainerDao, times(1)).findById(trainer.getUserId());
    verify(trainingDao, times(1)).save(training);
  }

  @Test
  void saveTrainingIfInvalidTraineeId() {
    var training =
        new Training(
            "0", "1234", "testTraining", TrainingType.CROSSFIT, LocalDate.of(2025, 1, 25), 60);
    var trainer =
        new Trainer(
            "TestName", "TestSurname", "testUsername", "password", true, TrainingType.BOXING);
    trainer.setUserId(training.getTrainerId());

    when(traineeDao.findById(training.getTraineeId())).thenReturn(Optional.empty());

    var exception =
        assertThrows(TrainingServiceException.class, () -> trainingService.save(training));

    assertThat(exception.getClass()).isEqualTo(TrainingServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("This trainee ID doesn't exist!");

    verify(traineeDao, times(1)).findById(training.getTraineeId());
    verifyNoInteractions(trainerDao);
    verifyNoInteractions(trainingDao);
  }

  @Test
  void saveTrainingIfInvalidTrainerId() {
    var training =
        new Training(
            "1234", "0", "testTraining", TrainingType.CROSSFIT, LocalDate.of(2025, 1, 25), 60);
    var trainee =
        new Trainee(
            "TestName",
            "TestSurname",
            "testusername",
            "testpassword",
            true,
            LocalDate.of(2000, 2, 20),
            "Test Address");
    trainee.setUserId(training.getTraineeId());

    when(traineeDao.findById(training.getTraineeId())).thenReturn(Optional.of(trainee));
    when(trainerDao.findById(training.getTrainerId())).thenReturn(Optional.empty());

    var exception =
        assertThrows(TrainingServiceException.class, () -> trainingService.save(training));

    assertThat(exception.getClass()).isEqualTo(TrainingServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("This trainer ID doesn't exist!");

    verify(traineeDao, times(1)).findById(trainee.getUserId());
    verify(trainerDao, times(1)).findById(training.getTrainerId());
    verifyNoInteractions(trainingDao);
  }
}
