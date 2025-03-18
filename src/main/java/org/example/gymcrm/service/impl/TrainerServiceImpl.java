package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.mapper.TrainerMapper;
import org.example.gymcrm.security.service.JwtService;
import org.example.gymcrm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
  private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
  private static final String TRAINER_NOT_FOUND = "This trainer doesn't exist!";
  private static final String TRAINEE_NOT_FOUND = "This trainee doesn't exist!";
  private final TrainerDao trainerDao;
  private final TraineeDao traineeDao;
  private final TrainingTypeDao trainingTypeDao;
  private final JwtService jwtService;
  private final TrainerMapper trainerMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public RegisterTrainerResponseDto save(RegisterTrainerRequestDto trainerRequestDto) {
    logger.info("Attempting to register a new trainer");
    var user = trainerMapper.registerDtoToUser(trainerRequestDto);
    var trainer = new Trainer();
    trainer.setUser(user);

    var trainingType = getTrainingType(trainerRequestDto.getSpecializationId());

    var rawPassword = generateRandomPassword();
    trainer.setSpecialization(trainingType);
    assignGeneratedCredentials(trainer, rawPassword);

    var savedTrainer = trainerDao.save(trainer);
    var responseDto = trainerMapper.trainerToDto(savedTrainer.getUser());
    responseDto.setPassword(rawPassword);

    var jwt = jwtService.generateToken(savedTrainer.getUser());

    logger.info("Successfully registered trainer with username: {}", responseDto.getUsername());

    return RegisterTrainerResponseDto.builder()
        .username(savedTrainer.getUser().getUsername())
        .password(rawPassword)
        .token(jwt)
        .build();
  }

  private void assignGeneratedCredentials(Trainer trainer, String passwordToEncode) {
    var usernames = mergeAllUsernames(traineeDao.findUsernames(), trainerDao.findUsernames());
    var user = trainer.getUser();

    user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), usernames));
    user.setPassword(passwordEncoder.encode(passwordToEncode));

    logger.info("Successfully assigned credentials for new trainer");
  }

  private TrainingType getTrainingType(Long specializationId) {
    logger.info("Fetching specialization by ID: {}", specializationId);
    return trainingTypeDao
        .findById(specializationId)
        .orElseThrow(() -> new TrainerServiceException("Specialization not found"));
  }

  @Transactional
  @Override
  public TrainerProfileDto update(String username, UpdateTrainerRequestDto trainer) {
    logger.info("Updating trainer with username: {}", username);
    var existingTrainer = getTrainerByUsername(username);
    logger.info("Found trainer: {}", username);

    updateTrainerFields(trainer, existingTrainer);

    var updatedTrainer = trainerDao.update(existingTrainer);
    var trainerProfile = createTrainerProfile(updatedTrainer);
    trainerProfile.setUsername(existingTrainer.getUser().getUsername());

    logger.info("Successfully updated trainer with username: {}", username);
    return trainerProfile;
  }

  private TrainerProfileDto createTrainerProfile(Trainer updatedTrainer) {
    var trainerProfile = trainerMapper.toProfileDto(updatedTrainer);
    setTraineesToTrainerProfile(updatedTrainer, trainerProfile);
    return trainerProfile;
  }

  private void updateTrainerFields(
      UpdateTrainerRequestDto updatedTrainer, Trainer existingTrainer) {
    var user = existingTrainer.getUser();
    assignSpecialization(existingTrainer, updatedTrainer.getSpecializationId());
    updateUserFields(updatedTrainer, user);
  }

  private void assignSpecialization(Trainer trainer, Long id) {
    logger.info("Assigning specialization to trainer");
    var specialization =
        trainingTypeDao
            .findById(id)
            .orElseThrow(
                () -> {
                  logger.error("Spicialization with id: {} not found", id);
                  return new TrainerServiceException("Specialization not found");
                });
    trainer.setSpecialization(specialization);
  }

  private void updateUserFields(UpdateTrainerRequestDto trainer, User user) {
    user.setFirstName(trainer.getFirstName());
    user.setLastName(trainer.getLastName());
    user.setActive(trainer.getActive());
  }

  @Transactional(readOnly = true)
  @Override
  public TrainerProfileDto findByUsername(String username) {
    var trainer = getTrainerByUsername(username);

    var trainerProfile = trainerMapper.toProfileDto(trainer);
    setTraineesToTrainerProfile(trainer, trainerProfile);

    logger.info("Found trainer with username: {}", username);
    return trainerProfile;
  }

  private void setTraineesToTrainerProfile(Trainer trainer, TrainerProfileDto trainerProfile) {
    var trainerTrainees =
        trainer.getTrainees().stream().map(trainerMapper::toTrainerTraineesDto).toList();
    trainerProfile.setTrainees(trainerTrainees);
  }

  private Trainer getTrainerByUsername(String trainerUsername) {
    logger.info("Finding trainer with username: {}", trainerUsername);
    return trainerDao
        .findByUsername(trainerUsername)
        .orElseThrow(
            () -> {
              logger.error("{} trainer doesn't exist", trainerUsername);
              return new NotFoundException(TRAINER_NOT_FOUND);
            });
  }

  private Trainee getTraineeByUsername(String traineeUsername) {
    logger.info("Finding trainee with username: {}", traineeUsername);
    return traineeDao
        .findByUsername(traineeUsername)
        .orElseThrow(
            () -> {
              logger.error("{} trainee doesn't exist", traineeUsername);
              return new NotFoundException(TRAINEE_NOT_FOUND);
            });
  }

  @Transactional
  @Override
  public void changeStatus(String username) {
    logger.info("Changing status for trainer with username: {}", username);
    var existingTrainer = getTrainerByUsername(username);
    var oppositeStatus = !existingTrainer.getUser().isActive();

    existingTrainer.getUser().setActive(oppositeStatus);
    logger.info("Changed status for trainer with username: {} to: {}", username, oppositeStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerProfileDto> getUnassignedTrainers(String username) {
    logger.info("Fetching unassigned trainers for trainee: {}", username);
    getTraineeByUsername(username);
    var trainers = trainerDao.findUnassignedTrainersByTraineeUsername(username);

    var trainersProfiles = trainers.stream().map(trainerMapper::toProfileDtoForUnassigned).toList();

    logger.info("Successfully fetched unassigned trainers by trainee: {}", username);
    return trainersProfiles;
  }
}
