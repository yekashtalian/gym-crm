package org.example.gymcrm.service;

import java.util.Date;
import java.util.List;

import org.example.gymcrm.dto.TrainingDTO;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;

public interface TrainingService {
  List<TrainingDTO> getAll();
  void save(Training training);
  List<TrainingDTO> getTrainingsByTraineeUsername(
          String username, Date fromDate, Date toDate, String firstName);

  List<TrainingDTO> getTrainingsByTrainerUsername(
          String username, Date fromDate, Date toDate, TrainingType.Type type, String firstName);
}
