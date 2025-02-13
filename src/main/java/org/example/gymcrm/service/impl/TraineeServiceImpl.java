package org.example.gymcrm.service.impl;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dto.TraineeProfileDTO;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.example.gymcrm.util.ProfileUtils.*;

@Service
public class TraineeServiceImpl implements TraineeService {
  private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
  private static final String TRAINEE_NOT_FOUND = "This trainee doesn't exist!";
  @Autowired private TraineeDao traineeDao;
  @Autowired private TrainerDao trainerDao;

  @Transactional
  @Override
  public void save(Trainee trainee) {
    if (trainee.getId() == null) {
      assignGeneratedCredentials(trainee);
      traineeDao.save(trainee);
    } else {
      traineeDao.update(trainee);
    }
    logger.info("Successfully saved {}", trainee);
  }

  @Transactional
  @Override
  public void update(Long id, Trainee trainee) {
    var existingTrainee =
        traineeDao.findById(id).orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    updateTraineeFields(trainee, existingTrainee);
    traineeDao.update(existingTrainee);
    logger.info("Successfully updated trainee with id = {}", id);
  }

  private static void updateTraineeFields(Trainee trainee, Trainee existingTrainee) {
    existingTrainee.getUser().setFirstName(trainee.getUser().getFirstName());
    existingTrainee.getUser().setLastName(trainee.getUser().getLastName());
    existingTrainee.getUser().setUsername(trainee.getUser().getUsername());
    existingTrainee.setDateOfBirth(trainee.getDateOfBirth());
    existingTrainee.setAddress(trainee.getAddress());
  }

  @Transactional
  @Override
  public void deleteByUsername(String username) {
    traineeDao
        .findByUsername(username)
        .orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    traineeDao.deleteByUsername(username);
    logger.info("Successfully removed trainee by {} username", username);
  }

  @Transactional
  @Override
  public void changePassword(String oldPassword, String newPassword, String username) {
    var existingTrainee =
        traineeDao
            .findByUsername(username)
            .orElseThrow(() -> new TrainerServiceException(TRAINEE_NOT_FOUND));

    if (!existingTrainee.getUser().getPassword().equals(oldPassword)) {
      throw new TrainerServiceException("Trainer current password doesnt match with old password");
    }

    existingTrainee.getUser().setPassword(newPassword);
    logger.info("Successfully changed password for trainee with {} username", username);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TraineeProfileDTO> findAll() {
    var traineesProfiles = traineeDao.findAll().stream().map(this::mapToDto).toList();
    logger.info("Successfully fetched all trainees");
    return traineesProfiles;
  }

  private TraineeProfileDTO mapToDto(Trainee trainee) {
    return new TraineeProfileDTO(
        trainee.getId(),
        trainee.getUser().getFirstName(),
        trainee.getUser().getLastName(),
        trainee.getUser().getUsername(),
        trainee.getUser().getPassword(),
        trainee.getUser().isActive(),
        trainee.getDateOfBirth(),
        trainee.getAddress());
  }

  @Transactional(readOnly = true)
  @Override
  public TraineeProfileDTO findByUsername(String username) {
    var existingTrainee =
        traineeDao
            .findByUsername(username)
            .orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));

    var traineeProfileDto = new TraineeProfileDTO();
    traineeProfileDto.setId(existingTrainee.getId());
    traineeProfileDto.setFirstName(existingTrainee.getUser().getFirstName());
    traineeProfileDto.setLastName(existingTrainee.getUser().getLastName());
    traineeProfileDto.setUsername(existingTrainee.getUser().getUsername());
    traineeProfileDto.setPassword(existingTrainee.getUser().getPassword());
    traineeProfileDto.setActive(existingTrainee.getUser().isActive());
    traineeProfileDto.setDateOfBirth(existingTrainee.getDateOfBirth());
    traineeProfileDto.setAddress(existingTrainee.getAddress());

    logger.info("Found trainee with {} username", username);
    return traineeProfileDto;
  }

  @Transactional
  @Override
  public void changeStatus(Long id) {
    var existingTrainee =
        traineeDao.findById(id).orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    var oppositeStatus = !existingTrainee.getUser().isActive();

    existingTrainee.getUser().setActive(oppositeStatus);
    logger.info("Changed status for trainee with id {}", id);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean authenticate(String username, String password) {
    Optional<Trainee> trainee = traineeDao.findByUsername(username);
    if (trainee.isPresent() && trainee.get().getUser().getPassword().equals(password)) {
      logger.info("Successfully authenticated trainee");
      return true;
    }
    throw new AuthenticationException("Invalid username or password!");
  }

  @Transactional
  @Override
  public void addTrainerToList(String traineeUsername, String trainerUsername) {
    var trainee =
        traineeDao
            .findByUsername(traineeUsername)
            .orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    var trainer =
        trainerDao
            .findByUsername(trainerUsername)
            .orElseThrow(() -> new TraineeServiceException("This trainer doesn't exist"));

    if (trainee.getTrainers().contains(trainer)) {
      throw new TraineeServiceException("This trainer is already in trainee's favorite list");
    }
    trainee.addTrainer(trainer);
    logger.info(
        "Successfully added trainer: {} to trainee: {} list", trainerUsername, traineeUsername);
  }

  @Transactional
  @Override
  public void removeTrainerFromList(String traineeUsername, String trainerUsername) {
    var trainee =
        traineeDao
            .findByUsername(traineeUsername)
            .orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    var trainer =
        trainerDao
            .findByUsername(trainerUsername)
            .orElseThrow(() -> new TraineeServiceException("This trainer doesn't exist"));
    if (trainee.getTrainers().contains(trainer)) {
      trainee.removeTrainer(trainer);
      logger.info(
          "Successfully removed trainer: {} from traine: {}", trainerUsername, traineeUsername);
    } else {
      throw new TraineeServiceException("This trainer is not in trainee favorite list");
    }
  }

  private void assignGeneratedCredentials(Trainee trainee) {
    var usernames = mergeAllUsernames(traineeDao.findUsernames(), trainerDao.findUsernames());
    var user = trainee.getUser();

    user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), usernames));
    user.setPassword(generateRandomPassword());
  }
}
