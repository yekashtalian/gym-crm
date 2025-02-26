package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.mapper.TraineeMapper;
import org.example.gymcrm.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public TraineeProfileDto update(String username, UpdateTraineeRequestDto trainee) {
    var existingTrainee = getTraineeByUsername(username);

    updateTraineeFields(trainee, existingTrainee);

    var updatedTrainee = traineeDao.update(existingTrainee);

    var traineeProfile = createTraineeProfile(updatedTrainee);
    traineeProfile.setUsername(existingTrainee.getUser().getUsername());

    logger.info("Successfully updated trainee with username = {}", username);
    return traineeProfile;
  }

  private void updateTraineeFields(UpdateTraineeRequestDto trainee, Trainee existingTrainee) {
    var user = existingTrainee.getUser();
    updateUserFields(trainee, user);

    Optional.ofNullable(trainee.getDateOfBirth()).ifPresent(existingTrainee::setDateOfBirth);
    Optional.ofNullable(trainee.getAddress()).ifPresent(existingTrainee::setAddress);
  }

  private void updateUserFields(UpdateTraineeRequestDto trainee, User user) {
    user.setFirstName(trainee.getFirstName());
    user.setLastName(trainee.getLastName());
    user.setActive(trainee.getActive());
  }

  private TraineeProfileDto createTraineeProfile(Trainee trainee) {
    var traineeProfile = traineeMapper.toProfileDto(trainee);
    setTrainersToTraineeProfile(trainee, traineeProfile);
    return traineeProfile;
  }

  private void setTrainersToTraineeProfile(Trainee trainee, TraineeProfileDto traineeProfile) {
    var traineeTrainers =
        trainee.getTrainers().stream().map(traineeMapper::toTraineeTrainersDto).toList();
    traineeProfile.setTrainers(traineeTrainers);
  }

  @Transactional
  @Override
  public void deleteByUsername(String username) {
    getTraineeByUsername(username);
    traineeDao.deleteByUsername(username);
    logger.info("Successfully removed trainee by {} username", username);
  }

  @Transactional(readOnly = true)
  @Override
  public TraineeProfileDto findByUsername(String username) {
    var trainee = getTraineeByUsername(username);

    var traineeProfile = createTraineeProfile(trainee);

    logger.info("Found trainee with {} username", username);

    return traineeProfile;
  }

  @Transactional
  @Override
  public void changeStatus(String username) {
    var existingTrainee = getTraineeByUsername(username);
    var oppositeStatus = !existingTrainee.getUser().isActive();

    existingTrainee.getUser().setActive(oppositeStatus);
    logger.info("Changed status for trainee with username {}", username);
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

  @Transactional
  @Override
  public List<TraineeTrainersDto> updateTraineeTrainers(
      String username, UpdateTrainersDto updateTrainersDto) {
    Trainee existingTrainee = getTraineeByUsername(username);
    Set<Trainer> trainers = extractTrainers(updateTrainersDto.getTrainers());

    existingTrainee.setTrainers(trainers);
    traineeDao.update(existingTrainee);

    return trainers.stream().map(traineeMapper::toTraineeTrainersDto).collect(Collectors.toList());
  }

  private Set<Trainer> extractTrainers(List<String> trainersUsernames) {
    if (trainersUsernames == null || trainersUsernames.isEmpty()) {
      return Collections.emptySet();
    }

    return trainersUsernames.stream().map(this::findTrainerByUsername).collect(Collectors.toSet());
  }

  private Trainer findTrainerByUsername(String username) {
    String cleanUsername = username.replaceAll("[\\[\\]\"]", "");
    return trainerDao
        .findByUsername(cleanUsername)
        .orElseThrow(() -> new TraineeServiceException("Trainer not found"));
  }

  private Trainer getTrainerByUsername(String trainerUsername) {
    return trainerDao
        .findByUsername(trainerUsername)
        .orElseThrow(() -> new NotFoundException("This trainer doesn't exist"));
  }

  private Trainee getTraineeByUsername(String traineeUsername) {
    return traineeDao
        .findByUsername(traineeUsername)
        .orElseThrow(() -> new NotFoundException(TRAINEE_NOT_FOUND));
  }
}
