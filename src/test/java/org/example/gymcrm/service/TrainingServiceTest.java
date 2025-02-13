package org.example.gymcrm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainingDTO;
import org.example.gymcrm.entity.*;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.exception.TrainingServiceException;
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

    @InjectMocks private TrainingServiceImpl trainingService;

    private Training training;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private User traineeUser;
    private User trainerUser;

    @BeforeEach
    void setUp() {
        traineeUser = new User();
        traineeUser.setUsername("trainee1");

        trainerUser = new User();
        trainerUser.setUsername("trainer1");

        trainee = new Trainee();
        trainee.setUser(traineeUser);

        trainer = new Trainer();
        trainer.setUser(trainerUser);

        trainingType = new TrainingType(TrainingType.Type.YOGA);

        training = new Training();
        training.setId(1L);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setType(trainingType);
        training.setName("Yoga Session");
        training.setDate(new Date());
        training.setDuration(60);
    }

    @Test
    void getAll_ShouldReturnListOfTrainingDTOs() {
        when(trainingDao.findAll()).thenReturn(List.of(training));
        List<TrainingDTO> result = trainingService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

//    @Test
//    void save_ShouldSaveTrainingWithValidSpecialization() {
//        when(trainingTypeDao.findByName(TrainingType.Type.YOGA)).thenReturn(Optional.of(trainingType));
//        trainingService.save(training);
//        verify(trainingDao, times(1)).save(training);
//    }
//
//    @Test
//    void save_ShouldThrowException_WhenSpecializationNotFound() {
//        when(trainingTypeDao.findByName(TrainingType.Type.YOGA)).thenReturn(Optional.empty());
//        assertThrows(TrainingServiceException.class, () -> trainingService.save(training));
//    }

    @Test
    void getTrainingsByTraineeUsername_ShouldReturnTrainingList() {
        when(traineeDao.findByUsername("trainee1")).thenReturn(Optional.of(trainee));
        when(trainingDao.getTrainingsByTraineeUsername(any(), any(), any(), any())).thenReturn(List.of(training));
        List<TrainingDTO> result = trainingService.getTrainingsByTraineeUsername("trainee1", new Date(), new Date(), "John");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTrainingsByTraineeUsername_ShouldThrowException_WhenTraineeNotFound() {
        when(traineeDao.findByUsername("trainee1")).thenReturn(Optional.empty());
        assertThrows(TrainingServiceException.class, () -> trainingService.getTrainingsByTraineeUsername("trainee1", new Date(), new Date(), "John"));
    }

    @Test
    void getTrainingsByTrainerUsername_ShouldReturnTrainingList() {
        when(trainingTypeDao.findByName(TrainingType.Type.YOGA)).thenReturn(Optional.of(trainingType));
        when(trainerDao.findByUsername("trainer1")).thenReturn(Optional.of(trainer));
        when(trainingDao.getTrainingsByTrainerUsername(any(), any(), any(), any(), any())).thenReturn(List.of(training));
        List<TrainingDTO> result = trainingService.getTrainingsByTrainerUsername("trainer1", new Date(), new Date(), TrainingType.Type.YOGA, "John");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTrainingsByTrainerUsername_ShouldThrowException_WhenSpecializationNotFound() {
        when(trainingTypeDao.findByName(TrainingType.Type.YOGA)).thenReturn(Optional.empty());
        assertThrows(TrainerServiceException.class, () -> trainingService.getTrainingsByTrainerUsername("trainer1", new Date(), new Date(), TrainingType.Type.YOGA, "John"));
    }

    @Test
    void getTrainingsByTrainerUsername_ShouldThrowException_WhenTrainerNotFound() {
        when(trainingTypeDao.findByName(TrainingType.Type.YOGA)).thenReturn(Optional.of(trainingType));
        when(trainerDao.findByUsername("trainer1")).thenReturn(Optional.empty());
        assertThrows(TrainingServiceException.class, () -> trainingService.getTrainingsByTrainerUsername("trainer1", new Date(), new Date(), TrainingType.Type.YOGA, "John"));
    }
}

