package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.example.gymcrm.dao.impl.TrainerDaoImpl;
import org.example.gymcrm.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerDaoTest {
  private static final String TRAINER_KEY = "Trainer";
  @Mock private Storage storage;
  private Map<String, List<Trainer>> trainerStorage;

  @InjectMocks private TrainerDao trainerDao = new TrainerDaoImpl();

  @BeforeEach
  void setUp() {
    trainerStorage = new HashMap<>();
    when(storage.getTrainerStorage()).thenReturn(trainerStorage);
  }

  @Test
  void saveTrainer() {
    var trainer = new Trainer();

    trainerDao.save(trainer);

    assertThat(trainerStorage).containsKey(TRAINER_KEY);
    assertThat(trainerStorage.get(TRAINER_KEY).size()).isEqualTo(1);
    assertThat(trainerStorage.get(TRAINER_KEY).get(0)).isEqualTo(trainer);
  }

  @Test
  void updateTrainer() {
    var existingTrainer = new Trainer();
    existingTrainer.setFirstName("OldName");
    trainerStorage.put("Trainer", new ArrayList<>(List.of(existingTrainer)));

    var updatedTrainer = new Trainer();
    updatedTrainer.setFirstName("NewName");

    trainerDao.update(existingTrainer.getUserId(), updatedTrainer);

    assertThat(trainerStorage.get(TRAINER_KEY).get(0).getFirstName()).isEqualTo("NewName");
  }

  @Test
  void findAllTrainers() {
    var trainer1 = new Trainer();
    var trainer2 = new Trainer();
    trainerStorage.put("Trainer", new ArrayList<>(List.of(trainer1, trainer2)));

    var result = trainerDao.findAll();

    assertThat(result.size()).isEqualTo(2);
    assertThat(result).contains(trainer1, trainer2);
  }

  @Test
  void findTrainerById() {
    var trainer = new Trainer();
    trainerStorage.put("Trainer", new ArrayList<>(List.of(trainer)));

    Optional<Trainer> result = trainerDao.findById(trainer.getUserId());

    assertThat(result).isPresent();
    assertThat(result.get().getUserId()).isEqualTo(trainer.getUserId());
  }

  @Test
  void findById_ShouldReturnEmptyIfTrainerDoesNotExist() {
    trainerStorage.put("Trainer", new ArrayList<>());

    Optional<Trainer> result = trainerDao.findById("abcd-1234");

    assertThat(result).isEmpty();
  }
}
