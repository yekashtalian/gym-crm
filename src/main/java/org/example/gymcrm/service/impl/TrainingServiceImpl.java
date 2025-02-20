package org.example.gymcrm.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainingDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Transactional(readOnly = true)
  @Override
  public List<TrainingDto> getAll() {
    var trainings = trainingDao.findAll().stream().map(this::mapToDto).toList();
    logger.info("Successfully fetched all trainings");
    return trainings;
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
  public List<TrainingDto> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String firstName) {
    getTraineeByUsername(username);
    var trainings =
        trainingDao.getTrainingsByTraineeUsername(username, fromDate, toDate, firstName).stream()
            .map(this::mapToDto)
            .toList();

    logger.info("Successfully fetched trainings for trainee: {}", username);
    return trainings;
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainingDto> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, TrainingType.Type type, String firstName) {
    getTrainerByUsername(username);
    var trainings =
        trainingDao
            .getTrainingsByTrainerUsername(username, fromDate, toDate, type, firstName)
            .stream()
            .map(this::mapToDto)
            .toList();

    logger.info("Successfully fetched trainings for trainer: {}", username);
    return trainings;
  }

  private TrainingDto mapToDto(Training training) {
    return new TrainingDto(
        training.getId(),
        training.getTrainee().getUser().getUsername(),
        training.getTrainer().getUser().getUsername(),
        training.getName(),
        training.getType().getName().name(),
        training.getDate(),
        training.getDuration());
  }
}
