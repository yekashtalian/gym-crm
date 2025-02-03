package org.example.gymcrm.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.exception.ProfileUtilsException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class TraineeServiceTest {
  @Mock private TraineeDao traineeDao;
  @Mock private TrainerDao trainerDao;
  @InjectMocks private TraineeService traineeService = new TraineeServiceImpl();

  @Test
  void findAllTrainees() {
    List<Trainee> expectedTrainees =
        List.of(
            new Trainee(
                "X", "X", "xxxxxx", "qwerty", true, LocalDate.of(2003, 4, 25), "Test Address1"),
            new Trainee(
                "Y", "Y", "yyyyyy", "qwerty123", false, LocalDate.of(2001, 5, 1), "Test Address2"),
            new Trainee(
                "Z", "Z", "zzzzzz", "123456", true, LocalDate.of(1999, 1, 10), "Test Address3"));
    when(traineeDao.findAll()).thenReturn(expectedTrainees);

    var actualTrainees = traineeService.getAll();

    assertThat(actualTrainees).hasSameSizeAs(expectedTrainees);
    assertAll(
        () -> assertThat(actualTrainees.get(0).getFirstName()).isEqualTo("X"),
        () -> assertThat(actualTrainees.get(1).getFirstName()).isEqualTo("Y"),
        () -> assertThat(actualTrainees.get(2).getFirstName()).isEqualTo("Z"));

    verify(traineeDao, times(1)).findAll();
  }

  @Test
  void findAllTraineesIfListIsEmpty() {
    List<Trainee> expectedTrainees = List.of();
    when(traineeDao.findAll()).thenReturn(expectedTrainees);

    var actualTrainees = traineeService.getAll();

    assertThat(actualTrainees).isNotNull();
    assertThat(actualTrainees).isEmpty();
    assertThat(actualTrainees).hasSameSizeAs(expectedTrainees);

    verify(traineeDao, times(1)).findAll();
  }

  @Test
  void saveTrainee() {
    var trainee = new Trainee("John", "Smith");
    doNothing().when(traineeDao).save(trainee);

    traineeService.save(trainee);

    verify(traineeDao, times(1)).save(trainee);
  }

  @ParameterizedTest
  @CsvSource({", testLastName", "testFirstName,", ","})
  void saveTraineeIfInvalidNames(String firstName, String lastName) {
    var trainee =
        new Trainee(
            firstName,
            lastName,
            "testusername",
            "testpassword",
            true,
            LocalDate.of(2000, 2, 20),
            "Test Address");

    var exception = assertThrows(ProfileUtilsException.class, () -> traineeService.save(trainee));

    assertThat(exception.getClass()).isEqualTo(ProfileUtilsException.class);
    assertThat(exception.getMessage()).isEqualTo("First or Last name cannot be empty or null");

    verifyNoInteractions(traineeDao);
  }

  @Test
  void deleteTrainee() {
    var id = "123";
    var trainee = getTrainee();

    when(traineeDao.findById(id)).thenReturn(Optional.of(trainee));
    doNothing().when(traineeDao).remove(trainee);

    traineeService.delete(id);

    verify(traineeDao, times(1)).findById(id);
    verify(traineeDao, times(1)).remove(trainee);
  }

  @Test
  void deleteTraineeIfTraineeDoesNotExist() {
    var id = "123";

    when(traineeDao.findById(id)).thenReturn(Optional.empty());

    var exception = assertThrows(TraineeServiceException.class, () -> traineeService.delete(id));

    assertThat(exception.getClass()).isEqualTo(TraineeServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("This trainee ID doesn't exist!");

    verify(traineeDao, times(1)).findById(id);
    verify(traineeDao, never()).remove(any());
  }

  @Test
  void updateTrainee() {
    var id = "123";
    var trainee = getTrainee();

    when(traineeDao.findById(id)).thenReturn(Optional.of(trainee));
    doNothing().when(traineeDao).update(id, trainee);

    traineeService.update(id, trainee);

    verify(traineeDao, times(1)).findById(id);
    verify(traineeDao, times(1)).update(id, trainee);
  }

  @Test
  void updateTraineeIfTraineeDoesNotExist() {
    var id = "123";
    var trainee = getTrainee();

    when(traineeDao.findById(id)).thenReturn(Optional.empty());
    var exception =
        assertThrows(TraineeServiceException.class, () -> traineeService.update(id, trainee));

    assertThat(exception.getClass()).isEqualTo(TraineeServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("This trainee ID doesn't exist!");

    verify(traineeDao, times(1)).findById(id);
    verify(traineeDao, never()).update(id, trainee);
  }

  private static Trainee getTrainee() {
    return new Trainee(
        "TestName",
        "TestSurname",
        "testusername",
        "testpassword",
        true,
        LocalDate.of(2000, 2, 20),
        "Test Address");
  }

  @Test
  void getUsername() {
    var usernames = List.of("trainee1", "trainee2");
    when(traineeDao.getUsernames()).thenReturn(usernames);

    var result = traineeService.getUsernames();

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).isEqualTo("trainee1");
    assertThat(result.get(1)).isEqualTo("trainee2");

    verify(traineeDao, times(2)).getUsernames();
  }

  @Test
  void getUsernamesIfUsernamesAreEmpty() {
    when(traineeDao.getUsernames()).thenReturn(List.of());

    var exception =
        assertThrows(TraineeServiceException.class, () -> traineeService.getUsernames());

    assertThat(exception.getClass()).isEqualTo(TraineeServiceException.class);
  }
}
