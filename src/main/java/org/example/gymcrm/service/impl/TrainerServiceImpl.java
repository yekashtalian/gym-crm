package org.example.gymcrm.service.impl;

import java.util.List;
import java.util.stream.Stream;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.util.ProfileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
  private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
  private static final String TRAINER_NOT_FOUND = "This trainer ID doesn't exist!";
  @Autowired private TrainerDao trainerDao;
  @Autowired private TraineeDao traineeDao;

  @Override
  public void save(Trainer trainer) {
    validateFirstAndLastName(trainer);

    setGeneratedUsername(trainer);
    setGeneratedPassword(trainer);

    trainerDao.save(trainer);
    logger.info("Successfully saved: {}", trainer);
  }

  private void setGeneratedPassword(Trainer trainer) {
    trainer.setPassword(ProfileUtils.generateRandomPassword());
  }

  private void setGeneratedUsername(Trainer trainer) {
    trainer.setUsername(
        ProfileUtils.generateUsername(
            trainer.getFirstName(),
            trainer.getLastName(),
            ProfileUtils.mergeAllUsernames(traineeDao.getUsernames(), trainerDao.getUsernames())));
  }

  @Override
  public void update(String id, Trainer trainer) {
    trainerDao.findById(id).orElseThrow(() -> new TrainerServiceException(TRAINER_NOT_FOUND));
    validateFirstAndLastName(trainer);
    trainerDao.update(id, trainer);
    logger.info("Successfully updated trainer with ID: {}", id);
  }

  @Override
  public List<Trainer> getAll() {
    var trainers = trainerDao.findAll();
    logger.info("Successfully retrieved trainers");
    return trainers;
  }

  @Override
  public List<String> getUsernames() {
    if (trainerDao.getUsernames().isEmpty()) {
      throw new TrainerServiceException("There are no usernames for trainers");
    }
    return trainerDao.getUsernames();
  }

  private static void validateFirstAndLastName(Trainer trainer) {
    if ((trainer.getFirstName() == null || trainer.getFirstName().isEmpty())
        || (trainer.getLastName() == null || trainer.getLastName().isEmpty())) {
      throw new TrainerServiceException("First or Last name cannot be empty or null");
    }
  }
}
