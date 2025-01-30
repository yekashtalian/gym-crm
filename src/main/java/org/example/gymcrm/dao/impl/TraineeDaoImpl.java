package org.example.gymcrm.dao.impl;

import org.example.gymcrm.dao.Storage;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TraineeDaoImpl implements TraineeDao {
  private static final String TRAINEE_KEY = "Trainee";
  private Storage storage;

  @Autowired
  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void save(Trainee trainee) {
    storage.getTraineeStorage().computeIfAbsent(TRAINEE_KEY, key -> new ArrayList<>()).add(trainee);
  }

  @Override
  public void update(String id, Trainee trainee) {
    var trainees = storage.getTraineeStorage().get(TRAINEE_KEY);

    trainees.stream()
        .filter(tr -> tr.getUserId().equals(id))
        .findFirst()
        .ifPresent(
            tr -> {
              tr.setFirstName(trainee.getFirstName());
              tr.setLastName(trainee.getLastName());
              tr.setUsername(trainee.getUsername());
              tr.setPassword(trainee.getPassword());
              tr.setIsActive(trainee.getIsActive());
              tr.setDateOfBirth(trainee.getDateOfBirth());
              tr.setAddress(trainee.getAddress());
            });
  }

  @Override
  public void remove(Trainee trainee) {
    storage.getTraineeStorage().get(TRAINEE_KEY).remove(trainee);
  }

  @Override
  public List<Trainee> findAll() {
    return storage.getTraineeStorage().get(TRAINEE_KEY);
  }

  @Override
  public Optional<Trainee> findById(String id) {
    return storage.getTraineeStorage().get(TRAINEE_KEY).stream()
        .filter(tr -> tr.getUserId().equals(id))
        .findFirst();
  }

  @Override
  public List<String> getUsernames() {
    return storage.getTraineeStorage().get(TRAINEE_KEY).stream().map(Trainee::getUsername).toList();
  }
}
