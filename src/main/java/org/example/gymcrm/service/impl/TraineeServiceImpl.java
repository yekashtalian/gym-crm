package org.example.gymcrm.service.impl;

import static org.example.gymcrm.util.ProfileUtils.*;

import java.util.List;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.service.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeServiceImpl implements TraineeService {
  private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
  private static final String TRAINEE_NOT_FOUND = "This trainee ID doesn't exist!";
  @Autowired private TraineeDao traineeDao;
  @Autowired private TrainerDao trainerDao;

  @Override
  public void save(Trainee trainee) {
    validateFirstAndLastName(trainee.getFirstName(), trainee.getLastName());

    setGeneratedPassword(trainee);
    setGeneratedUsername(trainee);

    traineeDao.save(trainee);
    logger.info("Successfully added trainee: {}", trainee);
  }

  @Override
  public void update(String id, Trainee trainee) {
    traineeDao.findById(id).orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    validateFirstAndLastName(trainee.getFirstName(), trainee.getLastName());
    traineeDao.update(id, trainee);
    logger.info("Successfully updated trainee with id: {}", id);
  }

  @Override
  public void delete(String id) {
    var traineeToDelete =
        traineeDao.findById(id).orElseThrow(() -> new TraineeServiceException(TRAINEE_NOT_FOUND));
    traineeDao.remove(traineeToDelete);
    logger.info("Successfully deleted trainee with id: {}", id);
  }

  @Override
  public List<Trainee> getAll() {
    var trainees = traineeDao.findAll();
    logger.info("Successfully retrieved trainees");
    return trainees;
  }

  @Override
  public List<String> getUsernames() {
    if (traineeDao.getUsernames().isEmpty()) {
      throw new TraineeServiceException("There are no usernames for trainees");
    }
    return traineeDao.getUsernames();
  }

  private void setGeneratedUsername(Trainee trainee) {
    trainee.setUsername(
        generateUsername(
            trainee.getFirstName(),
            trainee.getLastName(),
            mergeAllUsernames(traineeDao.getUsernames(), trainerDao.getUsernames())));
  }

  private void setGeneratedPassword(Trainee trainee) {
    trainee.setPassword(generateRandomPassword());
  }
}
