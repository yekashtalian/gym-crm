package org.example.gymcrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.Storage;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        .ifPresent(tr -> updateTraineeFields(tr, trainee));
  }

  private static void updateTraineeFields(Trainee existing, Trainee updated) {
    existing.setFirstName(updated.getFirstName());
    existing.setLastName(updated.getLastName());
    existing.setUsername(updated.getUsername());
    existing.setPassword(updated.getPassword());
    existing.setIsActive(updated.getIsActive());
    existing.setDateOfBirth(updated.getDateOfBirth());
    existing.setAddress(updated.getAddress());
  }

  @Override
  public void remove(Trainee trainee) {
    storage.getTraineeStorage().get(TRAINEE_KEY).remove(trainee);
  }

  @Override
  public List<Trainee> findAll() {
    var trainees = storage.getTraineeStorage().get(TRAINEE_KEY);
    return trainees;
  }

  @Override
  public Optional<Trainee> findById(String id) {
    Optional<Trainee> optionalTrainee =
        storage.getTraineeStorage().get(TRAINEE_KEY).stream()
            .filter(tr -> tr.getUserId().equals(id))
            .findFirst();
    return optionalTrainee;
  }

  @Override
  public List<String> getUsernames() {
    var usernames =
        storage.getTraineeStorage().get(TRAINEE_KEY).stream().map(Trainee::getUsername).toList();
    return usernames;
  }
}
