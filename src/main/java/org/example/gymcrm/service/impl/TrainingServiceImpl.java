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
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.NotFoundException;
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

  @Transactional
  @Override
  public void save(TrainingDto trainingDto) {
    logger.info("Attempting to save training: {}", trainingDto);
    var trainer = getTrainerByUsername(trainingDto.getTrainerUsername());
    var trainee = getTraineeByUsername(trainingDto.getTraineeUsername());

    var trainingToSave = trainingMapper.toTraining(trainingDto);
    trainingToSave.setTrainer(trainer);
    trainingToSave.setTrainee(trainee);

    trainingDao.save(trainingToSave);
    logger.info("Training saved successfully: {}", trainingToSave);
  }

  private Trainer getTrainerByUsername(String username) {
    logger.info("Fetching trainer by username: {}", username);
    return trainerDao
        .findByUsername(username)
        .orElseThrow(
            () -> {
              logger.error("Trainer not found: {}", username);
              return new NotFoundException("Trainer not found");
            });
  }

  private Trainee getTraineeByUsername(String username) {
    logger.info("Fetching trainee by username: {}", username);
    return traineeDao
        .findByUsername(username)
        .orElseThrow(
            () -> {
              logger.error("Trainee not found: {}", username);
              return new NotFoundException("Trainee not found");
            });
  }

  @Transactional(readOnly = true)
  @Override
  public List<TraineeTrainingDto> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String trainerName, String trainingTypeName) {
    logger.info("Fetching trainings for trainee: {}, from: {}, to: {}", username, fromDate, toDate);
    getTraineeByUsername(username);
    var trainingType = parseTrainingType(trainingTypeName);
    var trainings =
        trainingDao.getTrainingsByTraineeUsername(
            username, fromDate, toDate, trainerName, trainingType);

    var traineeTrainingsDto =
        trainings.stream().map(trainingMapper::toTraineeTrainingsDto).toList();

    logger.info(
        "Successfully fetched {} trainings for trainee: {}", traineeTrainingsDto.size(), username);

    return traineeTrainingsDto;
  }

  private TrainingType parseTrainingType(String trainingTypeName) {
    if (trainingTypeName == null || trainingTypeName.isBlank()) {
      return null;
    }
    logger.debug("Parsing training type: {}", trainingTypeName);
    return trainingTypeDao
        .findByName(TrainingType.Type.valueOf(trainingTypeName.toUpperCase()))
        .orElseThrow(
            () -> {
              logger.error("Training type not found: {}", trainingTypeName);
              return new NotFoundException("Training type not found");
            });
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerTrainingDto> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, String traineeName) {
    logger.info("Fetching trainings for trainer: {}, from: {}, to: {}", username, fromDate, toDate);
    getTrainerByUsername(username);
    var trainings =
        trainingDao.getTrainingsByTrainerUsername(username, fromDate, toDate, traineeName);

    var trainerTrainingsDto =
        trainings.stream().map(trainingMapper::toTrainerTrainingsDto).toList();

    logger.info(
        "Successfully fetched {} trainings for trainer: {}", trainerTrainingsDto.size(), username);

    return trainerTrainingsDto;
  }
}
