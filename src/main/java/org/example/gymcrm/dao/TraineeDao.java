package org.example.gymcrm.dao;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.Trainee;

public interface TraineeDao {
  void save(Trainee trainee);

  void update(String id, Trainee trainee);

  void remove(Trainee trainee);

  List<Trainee> findAll();

  Optional<Trainee> findById(String id);

  List<String> getUsernames();
}
