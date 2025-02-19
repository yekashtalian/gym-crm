package org.example.gymcrm.dao;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.Trainer;

public interface TrainerDao {
  Trainer save(Trainer trainer);

  List<String> findUsernames();

  Optional<Trainer> findByUsername(String username);

  Optional<Trainer> findById(Long id);

  Trainer update(Trainer trainer);

  List<Trainer> findUnassignedTrainersByTraineeUsername(String username);

  List<Trainer> findAll();
}
