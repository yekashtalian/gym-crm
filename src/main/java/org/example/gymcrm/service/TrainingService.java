package org.example.gymcrm.service;

import java.util.Date;
import java.util.List;

import org.example.gymcrm.dto.TraineeTrainingDto;
import org.example.gymcrm.dto.TrainerTrainingDto;
import org.example.gymcrm.dto.TrainingDto;

public interface TrainingService {

  void save(TrainingDto training);

  List<TraineeTrainingDto> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String trainerName, String trainingTypeName);

  List<TrainerTrainingDto> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, String traineeName);
}
