package org.example.gymcrm.service;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.enums.TrainingType;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
  @Mock private TrainerDao trainerDao;
  @Mock private TraineeDao traineeDao;
  @InjectMocks private TrainerService trainerService = new TrainerServiceImpl();

  @Test
  void findAllTrainers() {
    List<Trainer> expectedTrainers =
        List.of(
            new Trainer("X", "X", "xxxxxx", "password", true, TrainingType.BOXING),
            new Trainer("Y", "Y", "yyyyyy", "qwerty123", false, TrainingType.CROSSFIT),
            new Trainer("Z", "Z", "zzzzzz", "123456", true, TrainingType.DANCE));
    when(trainerDao.findAll()).thenReturn(expectedTrainers);

    var actualTrainers = trainerService.getAll();

    assertThat(actualTrainers).hasSameSizeAs(expectedTrainers);
    assertAll(
        () -> assertThat(actualTrainers.get(0).getFirstName()).isEqualTo("X"),
        () -> assertThat(actualTrainers.get(0).getSpecialization()).isEqualTo(TrainingType.BOXING),
        () -> assertThat(actualTrainers.get(1).getFirstName()).isEqualTo("Y"),
        () ->
            assertThat(actualTrainers.get(1).getSpecialization()).isEqualTo(TrainingType.CROSSFIT),
        () -> assertThat(actualTrainers.get(2).getFirstName()).isEqualTo("Z"),
        () -> assertThat(actualTrainers.get(2).getSpecialization()).isEqualTo(TrainingType.DANCE));

    verify(trainerDao, times(1)).findAll();
  }

  @Test
  void findAllTraineesIfListIsEmpty() {
    List<Trainer> expectedTrainers = List.of();
    when(trainerDao.findAll()).thenReturn(expectedTrainers);

    var actualTrainers = trainerService.getAll();

    assertThat(actualTrainers).isNotNull();
    assertThat(actualTrainers).isEmpty();

    verify(trainerDao, times(1)).findAll();
  }

  @Test
  void saveTrainer() {
    var trainer = new Trainer("John", "Smith");

    doNothing().when(trainerDao).save(trainer);

    trainerService.save(trainer);

    verify(trainerDao, times(1)).save(trainer);
  }

  @ParameterizedTest
  @CsvSource({", lastname", "firstname,", ","})
  void saveTrainerIfInvalidNames(String firstName, String lastName) {
    var trainer =
        new Trainer(firstName, lastName, "testUsername", "password", true, TrainingType.BOXING);

    var exception = assertThrows(TrainerServiceException.class, () -> trainerService.save(trainer));

    assertThat(exception.getClass()).isEqualTo(TrainerServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("First or Last name cannot be empty or null");

    verifyNoInteractions(trainerDao);
  }

  @Test
  void updateTrainer() {
    var id = "123";
    var trainer = getTrainer();
    when(trainerDao.findById(id)).thenReturn(Optional.of(trainer));
    doNothing().when(trainerDao).update(id, trainer);

    trainerService.update(id, trainer);

    verify(trainerDao, times(1)).findById(id);
    verify(trainerDao, times(1)).update(id, trainer);
  }

  @Test
  void updateTrainerIfTrainerDoesNotExist() {
    var id = "123";
    var trainer = getTrainer();
    when(trainerDao.findById(id)).thenReturn(Optional.empty());

    var exception =
        assertThrows(TrainerServiceException.class, () -> trainerService.update(id, trainer));

    assertThat(exception.getClass()).isEqualTo(TrainerServiceException.class);
    assertThat(exception.getMessage()).isEqualTo("This trainer ID doesn't exist!");

    verify(trainerDao, times(1)).findById(id);
    verify(trainerDao, never()).update(id, trainer);
  }

  private static Trainer getTrainer() {
    return new Trainer(
        "TestName", "TestSurname", "testUsername", "password", true, TrainingType.BOXING);
  }

  @Test
  void getUsernames() {
    var usernames = List.of("trainer1", "trainer2");
    when(trainerDao.getUsernames()).thenReturn(usernames);

    var result = trainerService.getUsernames();

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).isEqualTo("trainer1");
    assertThat(result.get(1)).isEqualTo("trainer2");

    verify(trainerDao, times(2)).getUsernames();
  }

  @Test
  void getUsernamesIfUsernamesAreEmpty() {
    when(trainerDao.getUsernames()).thenReturn(List.of());

    var exception = assertThrows(TrainerServiceException.class, () -> trainerService.getUsernames());

    assertThat(exception.getClass()).isEqualTo(TrainerServiceException.class);
  }
}
