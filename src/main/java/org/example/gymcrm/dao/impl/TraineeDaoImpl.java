package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDaoImpl implements TraineeDao {
  @PersistenceContext @Autowired private EntityManager entityManager;

  @Override
  public void save(Trainee trainee) {
    entityManager.persist(trainee);
  }

  @Override
  public List<String> findUsernames() {
    var usernames =
        entityManager
            .createQuery("select tr.user.username from Trainee tr", String.class)
            .getResultList();
    return usernames;
  }

  @Override
  public void deleteByUsername(String username) {
    entityManager
        .createQuery("delete from Trainee tr where tr.user.username = :username")
        .setParameter("username", username)
        .executeUpdate();
  }

  @Override
  public Optional<Trainee> findByUsername(String username) {
    try {
      var trainee =
          entityManager
              .createQuery(
                  "select tr from Trainee tr join fetch tr.user u where u.username = :username",
                  Trainee.class)
              .setParameter("username", username)
              .getSingleResult();
      return Optional.of(trainee);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    var trainee = entityManager.find(Trainee.class, id);
    return Optional.ofNullable(trainee);
  }

  @Override
  public void update(Trainee trainee) {
    entityManager.merge(trainee);
  }

  @Override
  public List<Trainee> findAll() {
    var trainees =
        entityManager
            .createQuery("select tr from Trainee tr join fetch tr.user", Trainee.class)
            .getResultList();
    return trainees;
  }
}
