package org.example.gymcrm.service.impl;

import java.util.Date;
import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainingDTO;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingServiceImpl implements TrainingService {
  private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
  @Autowired private TrainingDao trainingDao;
  @Autowired private TraineeDao traineeDao;
  @Autowired private TrainerDao trainerDao;
  @Autowired private TrainingTypeDao trainingTypeDao;
  private Validator validator;

  public TrainingServiceImpl() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainingDTO> getAll() {
    var trainings = trainingDao.findAll().stream().map(this::mapToDto).toList();
    logger.info("Successfully fetched all trainings");
    return trainings;
  }

  @Transactional
  @Override
  public void save(Training training) {
    validateTraining(training);
    logger.info("Saving training: {}", training);
    var trainee =
        traineeDao
            .findByUsername(training.getTrainee().getUser().getUsername())
            .orElseThrow(() -> new TrainingServiceException("Trainee not found"));

    var trainer =
        trainerDao
            .findByUsername(training.getTrainer().getUser().getUsername())
            .orElseThrow(() -> new TrainingServiceException("Trainer not found"));

    training.setTrainee(trainee);
    training.setTrainer(trainer);

    assignSpecialization(training);
    trainingDao.save(training);
    logger.info("Training saved successfully");
  }

  private void validateTraining(Training training) {
    for (ConstraintViolation<Training> violation : validator.validate(training)) {
      throw new ValidationException("Invalid trainee field: " + violation.getMessage());
    }
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
  public List<TrainingDTO> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String firstName) {
    logger.info("Fetching trainings for trainee: {}", username);
    traineeDao.findByUsername(username).orElseThrow(() -> new TrainingServiceException(""));
    var trainings =
        trainingDao.getTrainingsByTraineeUsername(username, fromDate, toDate, firstName).stream()
            .map(this::mapToDto)
            .toList();

    return trainings;
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainingDTO> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, TrainingType.Type type, String firstName) {
    logger.info("Fetching trainings for trainer: {}", username);
    trainingTypeDao
        .findByName(type)
        .orElseThrow(() -> new TrainerServiceException("Specialization not found"));

    trainerDao.findByUsername(username).orElseThrow(() -> new TrainingServiceException(""));
    var trainings =
        trainingDao
            .getTrainingsByTrainerUsername(username, fromDate, toDate, type, firstName)
            .stream()
            .map(this::mapToDto)
            .toList();

    return trainings;
  }

  private TrainingDTO mapToDto(Training training) {
    return new TrainingDTO(
        training.getId(),
        training.getTrainee().getUser().getUsername(),
        training.getTrainer().getUser().getUsername(),
        training.getName(),
        training.getType().getName().name(),
        training.getDate(),
        training.getDuration());
  }
}
