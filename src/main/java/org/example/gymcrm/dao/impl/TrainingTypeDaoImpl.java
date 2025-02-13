package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.entity.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {
  @PersistenceContext @Autowired private EntityManager entityManager;

  public Optional<TrainingType> findByName(TrainingType.Type type) {
    try {
      var trainingType =
          entityManager
              .createQuery("SELECT t FROM TrainingType t WHERE t.name = :name", TrainingType.class)
              .setParameter("name", type)
              .getSingleResult();
      return Optional.of(trainingType);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
