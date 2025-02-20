package org.example.gymcrm.service.impl;

import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TraineeTrainingDto;
import org.example.gymcrm.dto.TrainerTrainingDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.mapper.TrainingMapper;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
  private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
  private final TrainingDao trainingDao;
  private final TraineeDao traineeDao;
  private final TrainerDao trainerDao;
  private final TrainingTypeDao trainingTypeDao;
  private final TrainingMapper trainingMapper;

  @Transactional(readOnly = true)
  @Override
  public List<TraineeTrainingDto> getAll() {
    return null;
  }

  @Transactional
  @Override
  public void save(Training training) {
    var traineeUsername = training.getTrainee().getUser().getUsername();
    var trainerUsername = training.getTrainer().getUser().getUsername();
    var trainee = getTraineeByUsername(traineeUsername);
    var trainer = getTrainerByUsername(trainerUsername);
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    assignSpecialization(training);

    trainingDao.save(training);
    logger.info("Training saved successfully");
  }

  private Trainer getTrainerByUsername(String username) {
    return trainerDao
        .findByUsername(username)
        .orElseThrow(() -> new TrainingServiceException("Trainer not found"));
  }

  private Trainee getTraineeByUsername(String username) {
    return traineeDao
        .findByUsername(username)
        .orElseThrow(() -> new TrainingServiceException("Trainee not found"));
  }

  private void assignSpecialization(Training training) {
    logger.info("Assigning specialization for training");
    var trainingType =
        trainingTypeDao
            .findByName(training.getType().getName())
            .orElseThrow(() -> new TrainingServiceException("Training type not found"));
    training.setType(trainingType);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TraineeTrainingDto> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String trainerName, String trainingTypeName) {
    getTraineeByUsername(username);
    var trainingType = parseTrainingType(trainingTypeName);
    var trainings =
        trainingDao.getTrainingsByTraineeUsername(
            username, fromDate, toDate, trainerName, trainingType);

    var traineeTrainingsDto =
        trainings.stream().map(trainingMapper::toTraineeTrainingsDto).toList();

    logger.info("Successfully fetched trainings for trainee: {}", username);

    return traineeTrainingsDto;
  }

  private TrainingType parseTrainingType(String trainingTypeName) {
    TrainingType trainingType = null;
    if (trainingTypeName != null && !trainingTypeName.isBlank()) {
      trainingType =
          trainingTypeDao
              .findByName(TrainingType.Type.valueOf(trainingTypeName))
              .orElseThrow(() -> new TrainingServiceException("Training type not found"));
    }
    return trainingType;
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerTrainingDto> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, String traineeName) {
    getTrainerByUsername(username);
    var trainings =
        trainingDao.getTrainingsByTrainerUsername(username, fromDate, toDate, traineeName);

    var trainerTrainingsDto =
        trainings.stream().map(trainingMapper::toTrainerTrainingsDto).toList();

    logger.info("Successfully fetched trainings for trainee: {}", username);

    return trainerTrainingsDto;
  }
}
