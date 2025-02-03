package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.example.gymcrm.dao.impl.TrainingDaoImpl;
import org.example.gymcrm.entity.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingDaoTest {
  private static final String TRAINING_KEY = "Training";
  @Mock private Storage storage;
  private Map<String, List<Training>> trainingStorage;

  @InjectMocks private TrainingDao trainingDao = new TrainingDaoImpl();

  @BeforeEach
  void setUp() {
    trainingStorage = new HashMap<>();
    when(storage.getTrainingStorage()).thenReturn(trainingStorage);
  }

  @Test
  void saveTraining() {
    var training = new Training();

    trainingDao.save(training);

    assertThat(trainingStorage).containsKey(TRAINING_KEY);
    assertThat(trainingStorage.get(TRAINING_KEY).size()).isEqualTo(1);
    assertThat(trainingStorage.get(TRAINING_KEY).get(0)).isEqualTo(training);
  }

  @Test
  void findAllTrainings() {
    var training1 = new Training();
    var training2 = new Training();
    trainingStorage.put("Training", new ArrayList<>(List.of(training1, training2)));

    var result = trainingDao.findAll();

    assertThat(result.size()).isEqualTo(2);
    assertThat(result).contains(training1, training2);
  }

  @Test
  void findTrainingsById() {
    var training = new Training();
    trainingStorage.put("Training", new ArrayList<>(List.of(training)));

    Optional<Training> result = trainingDao.findById(training.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(training.getId());
  }

  @Test
  void findById_ShouldReturnEmptyIfTrainingDoesNotExist() {
    trainingStorage.put("Training", new ArrayList<>());

    Optional<Training> result = trainingDao.findById("abcd-1234");

    assertThat(result).isEmpty();
  }
}
