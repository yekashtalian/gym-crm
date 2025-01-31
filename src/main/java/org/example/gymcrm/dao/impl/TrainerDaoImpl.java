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
        .ifPresent(
            tr -> {
              tr.setFirstName(trainer.getFirstName());
              tr.setLastName(trainer.getLastName());
              tr.setUsername(trainer.getUsername());
              tr.setPassword(trainer.getPassword());
              tr.setIsActive(trainer.getIsActive());
              tr.setSpecialization(trainer.getSpecialization());
            });
  }

  @Override
  public List<Trainer> findAll() {
    return storage.getTrainerStorage().get(TRAINER_KEY);
  }

  @Override
  public Optional<Trainer> findById(String id) {
    return storage.getTrainerStorage().get(TRAINER_KEY).stream()
        .filter(tr -> tr.getUserId().equals(id))
        .findFirst();
  }

  @Override
  public List<String> getUsernames() {
    return storage.getTrainerStorage().get(TRAINER_KEY).stream().map(Trainer::getUsername).toList();
  }
}
