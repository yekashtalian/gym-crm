package org.example.gymcrm.ui;

import static org.mockito.Mockito.*;

import java.util.List;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MenuTest {

  @Mock private TraineeService traineeService;

  @Mock private TrainerService trainerService;

  @Mock private TrainingService trainingService;

  @InjectMocks private Menu menu;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void showAllTrainees_ShouldCallTraineeService() {
    when(traineeService.getAll()).thenReturn(List.of(new Trainee("1", "John", "Doe")));

    menu.showAllTrainees();

    verify(traineeService).getAll();
  }

  @Test
  void createTrainee_ShouldCallSaveMethod() {
    menu.createTrainee();
    verify(traineeService).save(any(Trainee.class));
  }

  @Test
  void updateTrainee_ShouldCallUpdateMethod() {
    menu.updateTrainee();
    verify(traineeService).update(anyString(), any(Trainee.class));
  }

  @Test
  void deleteTrainee_ShouldCallDeleteMethod() {
    menu.deleteTrainee();
    verify(traineeService).delete(anyString());
  }

  @Test
  void showAllTrainers_ShouldCallTrainerService() {
    when(trainerService.getAll()).thenReturn(List.of(new Trainer("John", "Smith")));

    menu.showAllTrainers();

    verify(trainerService).getAll();
  }

  @Test
  void createTrainer_ShouldCallSaveMethod() {
    menu.createTrainer();
    verify(trainerService).save(any(Trainer.class));
  }

  @Test
  void updateTrainer_ShouldCallUpdateMethod() {
    menu.updateTrainer();
    verify(trainerService).update(anyString(), any(Trainer.class));
  }

  @Test
  void showAllTrainings_ShouldCallTrainingService() {
    when(trainingService.getAll()).thenReturn(List.of(new Training()));

    menu.showAllTrainings();

    verify(trainingService).getAll();
  }

  @Test
  void createTraining_ShouldCallSaveMethod() {
    menu.createTraining();
    verify(trainingService).save(any(Training.class));
  }
}
