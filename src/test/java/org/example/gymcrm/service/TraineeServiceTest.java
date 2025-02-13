package org.example.gymcrm.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dto.TraineeProfileDTO;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.ProfileUtilsException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Mock private TraineeDao traineeDao;

  @Mock private TrainerDao trainerDao;

  @InjectMocks private TraineeServiceImpl traineeService;

  private Trainee trainee;
  private User user;

  @BeforeEach
  void setUp() throws ParseException {
    user = new User();
    user.setId(1L);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setUsername("john.doe");
    user.setPassword("password");
    user.setActive(true);

    trainee = new Trainee();
    trainee.setId(1L);
    trainee.setUser(user);
    trainee.setDateOfBirth(dateFormat.parse("1990-01-01"));
    trainee.setAddress("123 Main St");
  }

//  @Test
//  void testSave_NewTrainee() {
//    when(traineeDao.findUsernames()).thenReturn(Collections.emptyList());
//    when(trainerDao.findUsernames()).thenReturn(Collections.emptyList());
//    doNothing().when(traineeDao).save(any());
//
//    traineeService.save(trainee);
//
//    assertNotNull(trainee.getUser().getUsername());
//    assertNotNull(trainee.getUser().getPassword());
//    verify(traineeDao, times(1)).save(any());
//  }
//
//  @Test
//  void testSave_ExistingTrainee() {
//    trainee.setId(1L);
//    doNothing().when(traineeDao).save(trainee);
//
//    traineeService.save(trainee);
//
//    verify(traineeDao, times(1)).save(trainee);
//  }

  @Test
  void testUpdate() throws ParseException {
    when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

    Trainee updatedTrainee = new Trainee();
    updatedTrainee.setUser(new User());
    updatedTrainee.getUser().setFirstName("Jane");
    updatedTrainee.getUser().setLastName("Smith");
    updatedTrainee.setDateOfBirth(dateFormat.parse("1995-05-05"));
    updatedTrainee.setAddress("456 Elm St");

    traineeService.update(1L, updatedTrainee);

    assertEquals("Jane", trainee.getUser().getFirstName());
    assertEquals("Smith", trainee.getUser().getLastName());
    assertEquals(dateFormat.parse("1995-05-05"), trainee.getDateOfBirth());
    assertEquals("456 Elm St", trainee.getAddress());
    verify(traineeDao, times(1)).update(trainee);
  }

  @Test
  void testUpdate_TraineeNotFound() {
    when(traineeDao.findById(1L)).thenReturn(Optional.empty());

    assertThrows(TraineeServiceException.class, () -> traineeService.update(1L, trainee));
  }

  @Test
  void testDeleteByUsername() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

    traineeService.deleteByUsername("john.doe");

    verify(traineeDao, times(1)).deleteByUsername("john.doe");
  }

  @Test
  void testDeleteByUsername_TraineeNotFound() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(TraineeServiceException.class, () -> traineeService.deleteByUsername("john.doe"));
  }

  @Test
  void testChangePassword() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

    traineeService.changePassword("password", "newPassword", "john.doe");

    assertEquals("newPassword", trainee.getUser().getPassword());
  }

  @Test
  void testChangePassword_OldPasswordMismatch() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

    assertThrows(
        TrainerServiceException.class,
        () -> traineeService.changePassword("wrongPassword", "newPassword", "john.doe"));
  }

  @Test
  void testFindAll() {
    when(traineeDao.findAll()).thenReturn(List.of(trainee));

    List<TraineeProfileDTO> result = traineeService.findAll();

    assertEquals(1, result.size());
    assertEquals("John", result.get(0).getFirstName());
    assertEquals("Doe", result.get(0).getLastName());
  }

  @Test
  void testFindByUsername() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

    TraineeProfileDTO result = traineeService.findByUsername("john.doe");

    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
  }

  @Test
  void testFindByUsername_TraineeNotFound() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(TraineeServiceException.class, () -> traineeService.findByUsername("john.doe"));
  }

  @Test
  void testChangeStatus() {
    when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

    traineeService.changeStatus(1L);

    assertFalse(trainee.getUser().isActive());
  }

  @Test
  void testChangeStatus_TraineeNotFound() {
    when(traineeDao.findById(1L)).thenReturn(Optional.empty());

    assertThrows(TraineeServiceException.class, () -> traineeService.changeStatus(1L));
  }

  @Test
  void testAuthenticate_Success() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

    assertTrue(traineeService.authenticate("john.doe", "password"));
  }

  @Test
  void testAuthenticate_Failure() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

    assertThrows(
        AuthenticationException.class,
        () -> traineeService.authenticate("john.doe", "wrongPassword"));
  }

  @Test
  void testAuthenticate_TraineeNotFound() {
    when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.empty());

    assertThrows(
        AuthenticationException.class, () -> traineeService.authenticate("john.doe", "password"));
  }
}
