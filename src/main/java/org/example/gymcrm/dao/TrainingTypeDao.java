package org.example.gymcrm.dao;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.TrainingType;

public interface TrainingTypeDao {
  Optional<TrainingType> findByName(TrainingType.Type type);

  Optional<TrainingType> findById(Long id);

  List<TrainingType> findAll();
}
