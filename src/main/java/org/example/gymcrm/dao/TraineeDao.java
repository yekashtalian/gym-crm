package org.example.gymcrm.dao;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.Trainee;

public interface TraineeDao {
  Trainee save(Trainee trainee);

  List<String> findUsernames();

  void deleteByUsername(String username);

  Optional<Trainee> findByUsername(String username);

  Optional<Trainee> findById(Long id);

  Trainee update(Trainee trainee);
}
