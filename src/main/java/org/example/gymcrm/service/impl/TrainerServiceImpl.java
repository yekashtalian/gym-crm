package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.mapper.TrainerMapper;
import org.example.gymcrm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final TrainerMapper trainerMapper;

  @Transactional
  @Override
  public RegisterTrainerResponseDto save(RegisterTrainerRequestDto trainerRequestDto) {
    var user = trainerMapper.registerDtoToUser(trainerRequestDto);
    var trainer = new Trainer();
    trainer.setUser(user);

    var trainingType = getTrainingType(trainerRequestDto.getSpecializationId());
    trainer.setSpecialization(trainingType);
    assignGeneratedCredentials(trainer);

    var savedTrainer = trainerDao.save(trainer);
    var responseDto = trainerMapper.trainerToDto(savedTrainer.getUser());

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
  public TrainerProfileDto update(String username, UpdateTrainerRequestDto trainer) {
    var existingTrainer = getTrainerByUsername(username);

    updateTrainerFields(trainer, existingTrainer);

    var updatedTrainer = trainerDao.update(existingTrainer);

    var trainerProfile = createTrainerProfile(updatedTrainer);
    trainerProfile.setUsername(existingTrainer.getUser().getUsername());

    logger.info("Successfully updated trainee with id = {}", username);

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
    updateUserFields(updatedTrainer, user);
  }

  private void updateUserFields(UpdateTrainerRequestDto trainer, User user) {
    user.setFirstName(trainer.getFirstName());
    user.setLastName(trainer.getLastName());
    user.setActive(trainer.getActive());
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerProfileDto> getAll() {
    return null;
  }

  @Transactional(readOnly = true)
  @Override
  public TrainerProfileDto findByUsername(String username) {
    var trainer = getTrainerByUsername(username);

    var trainerProfile = trainerMapper.toProfileDto(trainer);
    setTraineesToTrainerProfile(trainer, trainerProfile);

    logger.info("Found trainee with {} username", username);
    return trainerProfile;
  }

  private void setTraineesToTrainerProfile(Trainer trainer, TrainerProfileDto trainerProfile) {
    var trainerTrainees =
        trainer.getTrainees().stream().map(trainerMapper::toTrainerTraineesDto).toList();
    trainerProfile.setTrainees(trainerTrainees);
  }

  private Trainer getTrainerByUsername(String username) {
    return trainerDao
        .findByUsername(username)
        .orElseThrow(() -> new NotFoundException(TRAINER_NOT_FOUND));
  }

  @Transactional
  @Override
  public void changeStatus(String username) {
    var existingTrainer = getTrainerByUsername(username);
    var oppositeStatus = !existingTrainer.getUser().isActive();

    existingTrainer.getUser().setActive(oppositeStatus);
    logger.info("Changed status for trainee with username {}", username);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerProfileDto> getUnassignedTrainers(String username) {
    traineeDao
        .findByUsername(username)
        .orElseThrow(() -> new NotFoundException("Trainee with such username doesn't exist"));
    var trainers = trainerDao.findUnassignedTrainersByTraineeUsername(username);

    var trainersProfiles = trainers.stream().map(trainerMapper::toProfileDtoForUnassigned).toList();

    logger.info("Successfully fetched unassigned trainers");

    return trainersProfiles;
  }
}
