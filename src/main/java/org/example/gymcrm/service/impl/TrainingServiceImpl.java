package org.example.gymcrm.service.impl;

import java.util.List;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {
  private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
  @Autowired private TrainingDao trainingDao;
  @Autowired private TraineeDao traineeDao;
  @Autowired private TrainerDao trainerDao;

  @Override
  public void save(Training training) {
    traineeDao
        .findById(training.getTraineeId())
        .orElseThrow(() -> new TrainingServiceException("This trainee ID doesn't exist!"));
    trainerDao
        .findById(training.getTrainerId())
        .orElseThrow(() -> new TrainingServiceException("This trainer ID doesn't exist!"));
    trainingDao.save(training);
    logger.info("Successfully added training: {}", training);
  }

  @Override
  public List<Training> getAll() {
    var trainings = trainingDao.findAll();
    logger.info("Successfully retrieved trainings");
    return trainings;
  }
}
