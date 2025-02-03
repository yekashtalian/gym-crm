package org.example.gymcrm.dao;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.Training;

public interface TrainingDao {
  void save(Training trainee);

  List<Training> findAll();

  Optional<Training> findById(String id);
}
