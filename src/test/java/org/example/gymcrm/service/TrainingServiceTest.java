package org.example.gymcrm.service;

import org.example.gymcrm.dao.*;
import org.example.gymcrm.dto.TrainingDTO;
import org.example.gymcrm.entity.*;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock private TrainingDao trainingDao;
    @Mock private TraineeDao traineeDao;
    @Mock private TrainerDao trainerDao;
    @Mock private TrainingTypeDao trainingTypeDao;

    @InjectMocks private TrainingServiceImpl trainingService;

    private Training training;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainee = new Trainee(new User("traineeUsername"));
        trainer = new Trainer(new User("trainerUsername"));
        trainingType = new TrainingType(TrainingType.Type.STRENGTH_TRAINING);

        training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setType(trainingType);
        training.setName("Strength Training");
        training.setDate(new Date());
        training.setDuration(60);
    }

    @Test
    void getAll_ShouldReturnListOfTrainings() {
        when(trainingDao.findAll()).thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService.getAll();

        assertEquals(1, result.size());
        assertEquals("traineeUsername", result.get(0).getTraineeUsername());
        assertEquals("trainerUsername", result.get(0).getTrainerUsername());
    }

    @Test
    void save_ShouldSaveTrainingSuccessfully() {
        when(traineeDao.findByUsername("traineeUsername")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainerUsername")).thenReturn(Optional.of(trainer));
        when(trainingTypeDao.findByName(TrainingType.Type.STRENGTH_TRAINING)).thenReturn(Optional.of(trainingType));

        trainingService.save(training);

        verify(trainingDao, times(1)).save(training);
    }

    @Test
    void save_ShouldThrowException_WhenTraineeNotFound() {
        when(traineeDao.findByUsername("traineeUsername")).thenReturn(Optional.empty());

        TrainingServiceException exception = assertThrows(TrainingServiceException.class,
                () -> trainingService.save(training));
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void save_ShouldThrowException_WhenTrainerNotFound() {
        when(traineeDao.findByUsername("traineeUsername")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainerUsername")).thenReturn(Optional.empty());

        TrainingServiceException exception = assertThrows(TrainingServiceException.class,
                () -> trainingService.save(training));
        assertEquals("Trainer not found", exception.getMessage());
    }

    @Test
    void getTrainingsByTraineeUsername_ShouldReturnTrainings() {
        when(traineeDao.findByUsername("traineeUsername")).thenReturn(Optional.of(trainee));
        when(trainingDao.getTrainingsByTraineeUsername(any(), any(), any(), any())).thenReturn(List.of(training));

        List<TrainingDTO> result = trainingService.getTrainingsByTraineeUsername("traineeUsername", new Date(), new Date(), "John");

        assertEquals(1, result.size());
    }

    @Test
    void getTrainingsByTraineeUsername_ShouldThrowException_WhenTraineeNotFound() {
        when(traineeDao.findByUsername("traineeUsername")).thenReturn(Optional.empty());

        assertThrows(TrainingServiceException.class,
                () -> trainingService.getTrainingsByTraineeUsername("traineeUsername", new Date(), new Date(), "John"));
    }
}


