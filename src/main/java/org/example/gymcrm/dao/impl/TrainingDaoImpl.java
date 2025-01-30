package org.example.gymcrm.dao.impl;

import org.example.gymcrm.dao.Storage;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {
  private static final String TRAINING_KEY = "Training";
  private Storage storage;

  @Autowired
  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void save(Training training) {
    storage
        .getTrainingStorage()
        .computeIfAbsent(TRAINING_KEY, key -> new ArrayList<>())
        .add(training);
  }

  @Override
  public List<Training> findAll() {
    return storage.getTrainingStorage().get(TRAINING_KEY);
  }

  @Override
  public Optional<Training> findById(String id) {
    return storage.getTrainingStorage().get(TRAINING_KEY).stream()
        .filter(tr -> tr.getId().equals(id))
        .findFirst();
  }
}
