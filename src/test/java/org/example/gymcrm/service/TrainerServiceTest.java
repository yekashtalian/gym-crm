package org.example.gymcrm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainerProfileDTO;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;
    private User user;
    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setActive(true);

        specialization = new TrainingType();
        specialization.setName(TrainingType.Type.STRENGTH_TRAINING);

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
    }

    @Test
    void testSave_NewTrainer() {
        when(trainingTypeDao.findByName(any())).thenReturn(Optional.of(specialization));
        doNothing().when(trainerDao).save(any());
        when(traineeDao.findUsernames()).thenReturn(List.of());
        when(trainerDao.findUsernames()).thenReturn(List.of());

        trainer.setId(null);
        trainerService.save(trainer);

        verify(trainerDao).save(any());
    }

    @Test
    void testSave_ExistingTrainer() {
        when(trainingTypeDao.findByName(any())).thenReturn(Optional.of(specialization));
        doNothing().when(trainerDao).update(any());

        trainerService.save(trainer);

        verify(trainerDao).update(any());
    }

    @Test
    void testUpdate_Success() {
        when(trainerDao.findById(anyLong())).thenReturn(Optional.of(trainer));
        when(trainingTypeDao.findByName(any())).thenReturn(Optional.of(specialization));
        doNothing().when(trainerDao).update(any());

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setUser(new User());
        updatedTrainer.getUser().setFirstName("Jane");
        updatedTrainer.getUser().setLastName("Smith");
        updatedTrainer.getUser().setUsername("janesmith");
        updatedTrainer.setSpecialization(specialization);

        trainerService.update(1L, updatedTrainer);

        verify(trainerDao).update(any());
        assertEquals("Jane", trainer.getUser().getFirstName());
    }

    @Test
    void testUpdate_TrainerNotFound() {
        when(trainerDao.findById(anyLong())).thenReturn(Optional.empty());

        Trainer updatedTrainer = new Trainer();
        assertThrows(TrainerServiceException.class, () -> trainerService.update(1L, updatedTrainer));
    }

    @Test
    void testFindByUsername_Success() {
        when(trainerDao.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        TrainerProfileDTO result = trainerService.findByUsername("johndoe");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testFindByUsername_NotFound() {
        when(trainerDao.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(TrainerServiceException.class, () -> trainerService.findByUsername("unknown"));
    }

    @Test
    void testChangePassword_Success() {
        when(trainerDao.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        trainerService.changePassword("password", "newpassword", "johndoe");

        assertEquals("newpassword", trainer.getUser().getPassword());
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        when(trainerDao.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        assertThrows(TrainerServiceException.class, () -> trainerService.changePassword("wrong", "newpassword", "johndoe"));
    }

    @Test
    void testChangeStatus_Success() {
        when(trainerDao.findById(anyLong())).thenReturn(Optional.of(trainer));

        trainerService.changeStatus(1L);

        assertFalse(trainer.getUser().isActive());
    }

    @Test
    void testGetUnassignedTrainers_Success() {
        when(trainerDao.findUnassignedTrainersByTraineeUsername(anyString())).thenReturn(List.of(trainer));

        List<Trainer> trainers = trainerService.getUnassignedTrainers("johndoe");

        assertEquals(1, trainers.size());
    }

    @Test
    void testGetUnassignedTrainers_EmptyList() {
        when(trainerDao.findUnassignedTrainersByTraineeUsername(anyString())).thenReturn(List.of());

        assertThrows(TrainerServiceException.class, () -> trainerService.getUnassignedTrainers("johndoe"));
    }

    @Test
    void testAuthenticate_Success() {
        when(trainerDao.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        assertTrue(trainerService.authenticate("johndoe", "password"));
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        when(trainerDao.findByUsername(anyString())).thenReturn(Optional.of(trainer));

        assertThrows(AuthenticationException.class, () -> trainerService.authenticate("johndoe", "wrong"));
    }
}
