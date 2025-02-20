package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl implements TrainingDao {
  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<Training> findAll() {
    var trainings =
        entityManager.createQuery("select tr from Training tr", Training.class).getResultList();
    return trainings;
  }

  @Override
  public void save(Training training) {
    entityManager.persist(training);
  }

  @Override
  public Optional<Training> findById(Long id) {
    var training = entityManager.find(Training.class, id);
    return Optional.ofNullable(training);
  }

  @Override
  public List<Training> getTrainingsByTraineeUsername(
      String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
    var trainings =
        entityManager
            .createQuery(
                """
                        SELECT t FROM Training t
                        JOIN t.trainee trainee
                        WHERE trainee.user.username = :username
                        AND (
                        :fromDate IS NULL OR t.date >= :fromDate
                        )
                        AND (
                        :toDate IS NULL OR t.date <= :toDate
                        )
                        AND (
                        :trainerName IS NULL OR t.trainer.user.firstName = :trainerName
                        )
                        AND (
                        :trainingType IS NULL OR t.type = :trainingType
                        )
                                                                         """,
                Training.class)
            .setParameter("username", username)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .setParameter("trainerName", trainerName)
            .setParameter("trainingType", trainingType)
            .getResultList();
    return trainings;
  }

  @Override
  public List<Training> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, String traineeName) {
    var trainings =
        entityManager
            .createQuery(
                """
                        SELECT t FROM Training t
                        JOIN FETCH t.trainer trainer
                        WHERE trainer.user.username = :username
                        AND (:fromDate IS NULL OR t.date >= :fromDate)
                        AND (:toDate IS NULL OR t.date <= :toDate)
                        AND (:traineeName IS NULL OR trainer.user.firstName = :traineeName)
                        """,
                Training.class)
            .setParameter("username", username)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .setParameter("traineeName", traineeName)
            .getResultList();
    return trainings;
  }
}
