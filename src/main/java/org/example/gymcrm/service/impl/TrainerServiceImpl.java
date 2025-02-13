package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainerProfileDTO;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.exception.AuthenticationException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerServiceImpl implements TrainerService {
  private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
  private static final String TRAINER_NOT_FOUND = "This trainer doesn't exist!";
  @Autowired private TrainerDao trainerDao;
  @Autowired private TraineeDao traineeDao;
  @Autowired private TrainingTypeDao trainingTypeDao;

  @Transactional
  @Override
  public void save(Trainer trainer) {
    assignSpecialization(trainer);
    if (trainer.getId() == null) {
      assignGeneratedCredentials(trainer);
      trainerDao.save(trainer);
    } else {
      trainerDao.update(trainer);
    }
    logger.info("Successfully saved {}", trainer);
  }

  private void assignSpecialization(Trainer trainer) {
    var specialization =
        trainingTypeDao
            .findByName(trainer.getSpecialization().getName())
            .orElseThrow(() -> new TrainerServiceException("Specialization not found"));
    trainer.setSpecialization(specialization);
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

  private void updateTrainerFields(Trainer source, Trainer target) {
    target.getUser().setFirstName(source.getUser().getFirstName());
    target.getUser().setLastName(source.getUser().getLastName());
    target.getUser().setUsername(source.getUser().getUsername());

    var existingSpecialization =
        trainingTypeDao
            .findByName(source.getSpecialization().getName())
            .orElseThrow(() -> new TrainerServiceException("Specialization not found"));

    target.setSpecialization(existingSpecialization);
  }

  @Override
  public List<TrainerProfileDTO> getAll() {
    var trainers = trainerDao.findAll().stream().map(this::mapToDto).toList();
    logger.info("Successfully fetched all trainees");
    return trainers;
  }

  private TrainerProfileDTO mapToDto(Trainer trainer) {
    return new TrainerProfileDTO(
        trainer.getId(),
        trainer.getUser().getFirstName(),
        trainer.getUser().getLastName(),
        trainer.getUser().getUsername(),
        trainer.getUser().getPassword(),
        trainer.getUser().isActive(),
        trainer.getSpecialization().getName().name());
  }

  @Override
  public TrainerProfileDTO findByUsername(String username) {
    var existingTrainer =
        trainerDao
            .findByUsername(username)
            .orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));

    var trainerProfileDto = new TrainerProfileDTO();
    trainerProfileDto.setId(existingTrainer.getId());
    trainerProfileDto.setFirstName(existingTrainer.getUser().getFirstName());
    trainerProfileDto.setLastName(existingTrainer.getUser().getLastName());
    trainerProfileDto.setUsername(existingTrainer.getUser().getUsername());
    trainerProfileDto.setPassword(existingTrainer.getUser().getPassword());
    trainerProfileDto.setActive(existingTrainer.getUser().isActive());
    trainerProfileDto.setSpecialization(existingTrainer.getSpecialization().getName().name());

    logger.info("Found trainee with {} username", username);
    return trainerProfileDto;
  }

  @Transactional
  @Override
  public void changePassword(String oldPassword, String newPassword, String username) {
    var existingTrainer =
        trainerDao
            .findByUsername(username)
            .orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));

    if (!existingTrainer.getUser().getPassword().equals(oldPassword)) {
      throw new TrainerServiceException("Trainer current password doesnt match with old password");
    }

    existingTrainer.getUser().setPassword(newPassword);
    logger.info("Successfully changed password for trainee with {} username", username);
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

  @Override
  public List<Trainer> getUnassignedTrainers(String username) {
    var trainers = trainerDao.findUnassignedTrainersByTraineeUsername(username);
    if (trainers.isEmpty()) {
      throw new TrainerServiceException("There is no unassigned trainers by this username");
    }
    logger.info("Successfully fetched unassigned trainers");
    return trainers;
  }

  @Override
  public boolean authenticate(String username, String password) {
    Optional<Trainer> trainer = trainerDao.findByUsername(username);
    if (trainer.isPresent() && trainer.get().getUser().getPassword().equals(password)) {
      logger.info("Successfully authenticated trainer");
      return true;
    }
    throw new AuthenticationException("Invalid username or password!");
  }

  private void assignGeneratedCredentials(Trainer trainer) {
    var usernames = mergeAllUsernames(traineeDao.findUsernames(), trainerDao.findUsernames());
    var user = trainer.getUser();

    user.setUsername(generateUsername(user.getFirstName(), user.getLastName(), usernames));
    user.setPassword(generateRandomPassword());
  }
}
