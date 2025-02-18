package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.dto.TraineeProfileDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.mapper.TraineeMapper;
import org.example.gymcrm.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
  private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
  private static final String TRAINEE_NOT_FOUND = "This trainee doesn't exist!";
  private final TraineeDao traineeDao;
  private final TrainerDao trainerDao;
  private final TraineeMapper traineeMapper;

  @Transactional
  @Override
  public RegisterTraineeResponseDto save(RegisterTraineeRequestDto traineeRequestDto) {
    var user = traineeMapper.registerDtoToUser(traineeRequestDto);
    var trainee = traineeMapper.registerDtoToTrainee(traineeRequestDto);

    trainee.setUser(user);
    assignGeneratedCredentials(trainee);

    var savedTrainee = traineeDao.save(trainee);
    var responseDto = traineeMapper.traineeToDto(savedTrainee.getUser());

    logger.info("Successfully registered trainee with username: {}", responseDto.getUsername());

    return responseDto;
  }

  private void assignGeneratedCredentials(Trainee trainee) {
    var usernames = mergeAllUsernames(traineeDao.findUsernames(), trainerDao.findUsernames());
    var user = trainee.getUser();

    user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), usernames));
    user.setPassword(generateRandomPassword());
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

  private void updateTraineeFields(Trainee trainee, Trainee existingTrainee) {
    var firstName = trainee.getUser().getFirstName();
    var lastName = trainee.getUser().getLastName();
    var username = trainee.getUser().getUsername();
    var dateOfBirth = trainee.getDateOfBirth();
    var address = trainee.getAddress();

    existingTrainee.getUser().setFirstName(firstName);
    existingTrainee.getUser().setLastName(lastName);
    if (trainee.getUser().getUsername() != null) {
      existingTrainee.getUser().setUsername(username);
    }
    if (trainee.getDateOfBirth() != null) {
      existingTrainee.setDateOfBirth(dateOfBirth);
    }
    if (trainee.getAddress() != null) {
      existingTrainee.setAddress(address);
    }
  }

  @Transactional
  @Override
  public void deleteByUsername(String username) {
    getTraineeByUsername(username);
    traineeDao.deleteByUsername(username);
    logger.info("Successfully removed trainee by {} username", username);
  }

  @Transactional
  @Override
  public void changePassword(String oldPassword, String newPassword, String username) {
    var existingTrainee = getTraineeByUsername(username);

    if (!existingTrainee.getUser().getPassword().equals(oldPassword)) {
      throw new TrainerServiceException("Trainer current password doesnt match with old password");
    }

    existingTrainee.getUser().setPassword(newPassword);
    logger.info("Successfully changed password for trainee with {} username", username);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TraineeProfileDto> findAll() {
    return null;
  }

  @Transactional(readOnly = true)
  @Override
  public TraineeProfileDto findByUsername(String username) {
    var trainee =
        traineeDao
            .findByUsername(username)
            .orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));

    var traineeProfile = traineeMapper.toProfileDto(trainee);
    var traineeTrainers =
        trainee.getTrainers().stream().map(traineeMapper::toTraineeTrainersDto).toList();

    traineeProfile.setTrainers(traineeTrainers);

    logger.info("Found trainee with {} username", username);

    return traineeProfile;
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
    traineeDao
        .findByUsername(username)
        .map(Trainee::getUser)
        .filter(user -> user.getPassword().equals(password))
        .orElseThrow(() -> new AuthenticationException("Invalid username or password!"));

    logger.info("Successfully authenticated trainee: {}", username);
    return true;
  }

  @Transactional
  @Override
  public void addTrainerToList(String traineeUsername, String trainerUsername) {
    var trainee = getTraineeByUsername(traineeUsername);
    var trainer = getTrainerByUsername(trainerUsername);

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
    var trainee = getTraineeByUsername(traineeUsername);
    var trainer = getTrainerByUsername(trainerUsername);

    if (trainee.getTrainers().contains(trainer)) {
      trainee.removeTrainer(trainer);
      logger.info(
          "Successfully removed trainer: {} from trainee: {}", trainerUsername, traineeUsername);
    } else {
      throw new TraineeServiceException("This trainer is not in trainee favorite list");
    }
  }

  private Trainer getTrainerByUsername(String trainerUsername) {
    return trainerDao
        .findByUsername(trainerUsername)
        .orElseThrow(() -> new TraineeServiceException("This trainer doesn't exist"));
  }

  private Trainee getTraineeByUsername(String traineeUsername) {
    return traineeDao
        .findByUsername(traineeUsername)
        .orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
  }
}
