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
  @PersistenceContext @Autowired private EntityManager entityManager;

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
      String username, Date fromDate, Date toDate, String firstName) {
    var trainings =
        entityManager
            .createQuery(
                """
                        SELECT t FROM Training t
                        JOIN t.trainee trainee
                        WHERE trainee.user.username = :username
                        AND (CAST(:fromDate AS date)) IS NULL OR t.date >= :fromDate
                        AND (CAST(:toDate AS date)) IS NULL OR t.date <= :toDate
                        AND (:firstName IS NULL OR trainee.user.firstName = :firstName)
                        """,
                Training.class)
            .setParameter("username", username)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .setParameter("firstName", firstName)
            .getResultList();
    return trainings;
  }

  @Override
  public List<Training> getTrainingsByTrainerUsername(
      String username, Date fromDate, Date toDate, TrainingType.Type type, String firstName) {
    var trainings =
        entityManager
            .createQuery(
                """
                            SELECT t FROM Training t
                            JOIN t.trainer trainer
                            JOIN t.trainer.specialization spec
                            WHERE trainer.user.username = :username
                            AND (CAST(:fromDate AS date)) IS NULL OR t.date >= :fromDate
                            AND (CAST(:toDate AS date)) IS NULL OR t.date <= :toDate
                            AND (:firstName IS NULL OR trainer.user.firstName = :firstName)
                            AND (:type IS NULL OR spec.name = :type)
                            """,
                Training.class)
            .setParameter("username", username)
            .setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .setParameter("type", type)
            .setParameter("firstName", firstName)
            .getResultList();
    return trainings;
  }
}
