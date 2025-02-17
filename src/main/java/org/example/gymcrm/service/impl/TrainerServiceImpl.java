package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.dto.TrainerProfileDto;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.mapper.RegisterTrainerMapper;
import org.example.gymcrm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
  private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
  private static final String TRAINER_NOT_FOUND = "This trainer doesn't exist!";
  private final TrainerDao trainerDao;
  private final TraineeDao traineeDao;
  private final TrainingTypeDao trainingTypeDao;
  private final RegisterTrainerMapper registerTrainerMapper;

  @Transactional
  @Override
  public RegisterTrainerResponseDto save(RegisterTrainerRequestDto trainerRequestDto) {
    var user = registerTrainerMapper.registerDtoToUser(trainerRequestDto);
    var trainer = new Trainer();
    trainer.setUser(user);
    var trainingType = getTrainingType(trainerRequestDto.getSpecializationId());
    trainer.setSpecialization(trainingType);

    assignGeneratedCredentials(trainer);

    var savedTrainer = trainerDao.save(trainer);
    var responseDto = registerTrainerMapper.trainerToDto(savedTrainer.getUser());

    logger.info("Successfully registered trainer with username: {}", responseDto.getUsername());

    return responseDto;
  }

  private void assignGeneratedCredentials(Trainer trainer) {
    var usernames = mergeAllUsernames(traineeDao.findUsernames(), trainerDao.findUsernames());
    var user = trainer.getUser();

    user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), usernames));
    user.setPassword(generateRandomPassword());
  }

  private TrainingType getTrainingType(Long specializationId) {
    var specialization =
        trainingTypeDao
            .findById(specializationId)
            .orElseThrow(() -> new TrainerServiceException("Specialization not found"));
    return specialization;
  }

  @Transactional
  @Override
  public void update(Long id, Trainer trainer) {
    var existingTrainer =
        trainerDao.findById(id).orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));

    updateTrainerFields(trainer, existingTrainer);
    trainerDao.update(existingTrainer);
    logger.info("Successfully updated trainee with id = {}", id);
  }

  private void updateTrainerFields(Trainer updatedTrainer, Trainer existingTrainer) {
    var firstName = updatedTrainer.getUser().getFirstName();
    var lastName = updatedTrainer.getUser().getLastName();
    var username = updatedTrainer.getUser().getUsername();
    var specialization = updatedTrainer.getSpecialization().getName();

    existingTrainer.getUser().setFirstName(firstName);
    existingTrainer.getUser().setLastName(lastName);
    if (existingTrainer.getUser().getUsername() != null) {
      existingTrainer.getUser().setUsername(username);
    }

    var existingSpecialization =
        trainingTypeDao
            .findByName(specialization)
            .orElseThrow(() -> new TrainerServiceException("Specialization not found"));
    existingTrainer.setSpecialization(existingSpecialization);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerProfileDto> getAll() {
    var trainers = trainerDao.findAll().stream().map(this::mapToDto).toList();
    logger.info("Successfully fetched all trainees");
    return trainers;
  }

  private TrainerProfileDto mapToDto(Trainer trainer) {
    return new TrainerProfileDto(
        trainer.getId(),
        trainer.getUser().getFirstName(),
        trainer.getUser().getLastName(),
        trainer.getUser().getUsername(),
        trainer.getUser().getPassword(),
        trainer.getUser().isActive(),
        trainer.getSpecialization().getName().name());
  }

  @Transactional(readOnly = true)
  @Override
  public TrainerProfileDto findByUsername(String username) {
    var trainerProfileDto =
        trainerDao
            .findByUsername(username)
            .map(this::mapToDto)
            .orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));

    logger.info("Found trainee with {} username", username);
    return trainerProfileDto;
  }

  @Transactional
  @Override
  public void changePassword(String oldPassword, String newPassword, String username) {
    var existingTrainer = getTrainerByUsername(username);

    if (!existingTrainer.getUser().getPassword().equals(oldPassword)) {
      throw new TrainerServiceException("Trainer current password doesnt match with old password");
    }

    existingTrainer.getUser().setPassword(newPassword);
    logger.info("Successfully changed password for trainee with {} username", username);
  }

  private Trainer getTrainerByUsername(String username) {
    return trainerDao
        .findByUsername(username)
        .orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));
  }

  @Transactional
  @Override
  public void changeStatus(Long id) {
    var existingTrainer =
        trainerDao.findById(id).orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));
    var oppositeStatus = !existingTrainer.getUser().isActive();

    existingTrainer.getUser().setActive(oppositeStatus);
    logger.info("Changed status for trainee with id {}", id);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Trainer> getUnassignedTrainers(String username) {
    var trainers =
        Optional.ofNullable(trainerDao.findUnassignedTrainersByTraineeUsername(username))
            .filter(trainerList -> !trainerList.isEmpty())
            .orElseThrow(
                () ->
                    new TrainerServiceException(
                        "There is no unassigned trainers by this username"));

    logger.info("Successfully fetched unassigned trainers");

    return trainers;
  }

  @Transactional(readOnly = true)
  @Override
  public boolean authenticate(String username, String password) {
    trainerDao
        .findByUsername(username)
        .map(Trainer::getUser)
        .filter(user -> user.getPassword().equals(password))
        .orElseThrow(() -> new AuthenticationException("Invalid username or password!"));

    logger.info("Successfully authenticated trainer: {}", username);
    return true;
  }
}
