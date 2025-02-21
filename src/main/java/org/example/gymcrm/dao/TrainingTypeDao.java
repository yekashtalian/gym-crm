package org.example.gymcrm.dao;

import org.example.gymcrm.entity.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeDao {
  Optional<TrainingType> findByName(TrainingType.Type type);

  Optional<TrainingType> findById(Long id);
  List<TrainingType> findAll();
}
