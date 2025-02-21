package org.example.gymcrm.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.entity.TrainingType;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {
  @PersistenceContext private EntityManager entityManager;

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

  @Override
  public Optional<TrainingType> findById(Long id) {
    var trainingType = entityManager.find(TrainingType.class, id);
    return Optional.ofNullable(trainingType);
  }

  @Override
  public List<TrainingType> findAll() {
    var trainingTypes = entityManager.createQuery("select tt from TrainingType tt").getResultList();
    return trainingTypes;
  }
}
