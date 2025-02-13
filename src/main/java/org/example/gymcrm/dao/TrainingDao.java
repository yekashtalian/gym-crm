package org.example.gymcrm.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;

public interface TrainingDao {
  List<Training> findAll();

  void save(Training training);

  Optional<Training> findById(Long id);

  List<Training> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String firstName);

  List<Training> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, TrainingType.Type type, String firstName);
}
