package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.example.gymcrm.dao.impl.TraineeDaoImpl;
import org.example.gymcrm.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeDaoTest {
  private static final String TRAINEE_KEY = "Trainee";

  @Mock private Storage storage;
  private Map<String, List<Trainee>> traineeStorage;

  @InjectMocks private TraineeDao traineeDao = new TraineeDaoImpl();

  @BeforeEach
  void setUp() {
    traineeStorage = new HashMap<>();
    when(storage.getTraineeStorage()).thenReturn(traineeStorage);
  }

  @Test
  void saveTrainee() {
    var trainee = new Trainee();

    traineeDao.save(trainee);

    assertThat(traineeStorage).containsKey(TRAINEE_KEY);
    assertThat(traineeStorage.size()).isEqualTo(1);
    assertThat(traineeStorage.get(TRAINEE_KEY).get(0)).isEqualTo(trainee);
  }

  @Test
  void updateTrainee() {
    var existingTrainee = new Trainee();
    existingTrainee.setFirstName("OldName");
    traineeStorage.put("Trainee", new ArrayList<>(List.of(existingTrainee)));

    var updatedTrainee = new Trainee();
    updatedTrainee.setFirstName("NewName");

    traineeDao.update(existingTrainee.getUserId(), updatedTrainee);

    assertThat(traineeStorage.get(TRAINEE_KEY).get(0).getFirstName()).isEqualTo("NewName");
  }

  @Test
  void removeTrainee() {
    var trainee = new Trainee();
    traineeStorage.put("Trainee", new ArrayList<>(List.of(trainee)));

    traineeDao.remove(trainee);

    assertThat(traineeStorage.get(TRAINEE_KEY)).isEmpty();
  }

  @Test
  void findAllTrainees() {
    var trainee1 = new Trainee();
    var trainee2 = new Trainee();
    traineeStorage.put("Trainee", new ArrayList<>(List.of(trainee1, trainee2)));

    var result = traineeDao.findAll();

    assertThat(result.size()).isEqualTo(2);
    assertThat(result).contains(trainee1, trainee2);
  }

  @Test
  void findTraineeById() {
    var trainee = new Trainee();
    traineeStorage.put("Trainee", new ArrayList<>(List.of(trainee)));

    Optional<Trainee> result = traineeDao.findById(trainee.getUserId());

    assertThat(result).isPresent();
    assertThat(result.get().getUserId()).isEqualTo(trainee.getUserId());
  }

  @Test
  void findTraineeByIdIfTraineeDoesNotExist() {
    traineeStorage.put("Trainee", new ArrayList<>());

    Optional<Trainee> result = traineeDao.findById("abcd-123");

    assertThat(result).isEmpty();
  }
}
