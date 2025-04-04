package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.entity.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl implements TrainerDao {
  @PersistenceContext private EntityManager entityManager;

  @Override
  public Trainer save(Trainer trainer) {
    entityManager.persist(trainer);
    return trainer;
  }

  @Override
  public List<String> findUsernames() {
    var usernames =
        entityManager
            .createQuery("select tr.user.username from Trainer tr", String.class)
            .getResultList();
    return usernames;
  }

  @Override
  public Optional<Trainer> findByUsername(String username) {
    try {
      var trainer = entityManager
              .createQuery(
                      "SELECT tr FROM Trainer tr JOIN FETCH tr.user u WHERE u.username = :username",
                      Trainer.class)
              .setParameter("username", username)
              .getSingleResult();

      return Optional.of(trainer);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }


  @Override
  public Optional<Trainer> findById(Long id) {
    var trainer = entityManager.find(Trainer.class, id);
    return Optional.ofNullable(trainer);
  }

  @Override
  public Trainer update(Trainer trainer) {
    return entityManager.merge(trainer);
  }

  @Override
  public List<Trainer> findUnassignedTrainersByTraineeUsername(String username) {
    List<Trainer> trainers =
        entityManager
            .createQuery(
                    """
                            select tr from Trainer tr
                            left join fetch tr.user
                            left join fetch tr.trainings
                            where tr not in (
                              select t.trainer from Training t
                              where t.trainee.user.username = :username
                            )
                            and tr.user.isActive = true
                            """,
                Trainer.class)
            .setParameter("username", username)
            .getResultList();
    return trainers;
  }
}
