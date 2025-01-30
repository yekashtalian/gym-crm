package org.example.gymcrm.dao;

import org.example.gymcrm.entity.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TraineeDao {
  void save(Trainee trainee);

  void update(String id, Trainee trainee);

  void remove(Trainee trainee);

  List<Trainee> findAll();

  Optional<Trainee> findById(String id);
  List<String> getUsernames();
}
