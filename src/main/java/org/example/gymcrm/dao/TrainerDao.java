package org.example.gymcrm.dao;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.Trainer;

public interface TrainerDao {
  void save(Trainer trainee);

  void update(String id, Trainer trainer);

  List<Trainer> findAll();

  Optional<Trainer> findById(String id);

  List<String> getUsernames();
}
