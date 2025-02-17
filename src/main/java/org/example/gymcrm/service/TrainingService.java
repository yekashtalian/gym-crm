package org.example.gymcrm.service;

import java.util.Date;
import java.util.List;

import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;

public interface TrainingService {
  List<TrainingDto> getAll();
  void save(Training training);
  List<TrainingDto> getTrainingsByTraineeUsername(
          String username, Date fromDate, Date toDate, String firstName);

  List<TrainingDto> getTrainingsByTrainerUsername(
          String username, Date fromDate, Date toDate, TrainingType.Type type, String firstName);
}
