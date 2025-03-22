package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.TemporalType;
import org.example.gymcrm.dao.TrainingDao;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
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

    StringBuilder jpql =
        new StringBuilder(
            """
        SELECT t FROM Training t
        JOIN t.trainee trainee
        WHERE trainee.user.username = :username
        """);

    if (fromDate != null) {
      jpql.append(" AND t.date >= :fromDate");
    }
    if (toDate != null) {
      jpql.append(" AND t.date <= :toDate");
    }
    if (trainerName != null) {
      jpql.append(" AND t.trainer.user.firstName = :trainerName");
    }
    if (trainingType != null) {
      jpql.append(" AND t.type = :trainingType");
    }

    var query =
        entityManager
            .createQuery(jpql.toString(), Training.class)
            .setParameter("username", username);

    if (fromDate != null) {
      query.setParameter("fromDate", fromDate, TemporalType.DATE);
    }
    if (toDate != null) {
      query.setParameter("toDate", toDate, TemporalType.DATE);
    }
    if (trainerName != null) {
      query.setParameter("trainerName", trainerName);
    }
    if (trainingType != null) {
      query.setParameter("trainingType", trainingType);
    }

    return query.getResultList();
  }

  @Override
  public List<Training> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, String traineeName) {

    StringBuilder jpql =
        new StringBuilder(
            """
                    SELECT t FROM Training t
                    JOIN t.trainer trainer
                    WHERE trainer.user.username = :username
                    """);

    if (fromDate != null) {
      jpql.append(" AND t.date >= :fromDate");
    }
    if (toDate != null) {
      jpql.append(" AND t.date <= :toDate");
    }
    if (traineeName != null) {
      jpql.append(" AND t.trainee.user.firstName = :traineeName");
    }

    var query =
        entityManager
            .createQuery(jpql.toString(), Training.class)
            .setParameter("username", username);

    if (fromDate != null) {
      query.setParameter("fromDate", fromDate, TemporalType.DATE);
    }
    if (toDate != null) {
      query.setParameter("toDate", toDate, TemporalType.DATE);
    }
    if (traineeName != null) {
      query.setParameter("traineeName", traineeName);
    }

    return query.getResultList();
  }
}
