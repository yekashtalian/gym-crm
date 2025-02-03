package org.example.gymcrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.Storage;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl implements TrainerDao {
  private static final String TRAINER_KEY = "Trainer";
  private Storage storage;

  @Autowired
  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void save(Trainer trainer) {
    storage.getTrainerStorage().computeIfAbsent(TRAINER_KEY, key -> new ArrayList<>()).add(trainer);
  }

  @Override
  public void update(String id, Trainer trainer) {
    var trainers = storage.getTrainerStorage().get(TRAINER_KEY);
    trainers.stream()
        .filter(tr -> tr.getUserId().equals(id))
        .findFirst()
        .ifPresent(tr -> updateTrainerFields(tr, trainer));
  }

  private static void updateTrainerFields(Trainer existing, Trainer updated) {
    existing.setFirstName(updated.getFirstName());
    existing.setLastName(updated.getLastName());
    existing.setUsername(updated.getUsername());
    existing.setPassword(updated.getPassword());
    existing.setIsActive(updated.getIsActive());
    existing.setSpecialization(updated.getSpecialization());
  }

  @Override
  public List<Trainer> findAll() {
    var trainers = storage.getTrainerStorage().get(TRAINER_KEY);
    return trainers;
  }

  @Override
  public Optional<Trainer> findById(String id) {
    Optional<Trainer> optionalTrainer =
        storage.getTrainerStorage().get(TRAINER_KEY).stream()
            .filter(tr -> tr.getUserId().equals(id))
            .findFirst();
    return optionalTrainer;
  }

  @Override
  public List<String> getUsernames() {
    var usernames =
        storage.getTrainerStorage().get(TRAINER_KEY).stream().map(Trainer::getUsername).toList();
    return usernames;
  }
}
