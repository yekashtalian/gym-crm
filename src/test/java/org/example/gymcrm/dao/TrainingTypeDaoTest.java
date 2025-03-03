package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.gymcrm.dao.impl.TrainingTypeDaoImpl;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.entity.TrainingType.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDaoTest {

  @Mock private EntityManager entityManager;

  @Mock private TypedQuery<TrainingType> query;

  @InjectMocks private TrainingTypeDaoImpl trainingTypeDao;

  private TrainingType trainingType;

  @BeforeEach
  void setUp() {
    trainingType = new TrainingType();
    trainingType.setId(1L);
    trainingType.setName(Type.CARDIO);
  }

  @Test
  void testFindByName_WhenTrainingTypeExists() {
    when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
    when(query.setParameter("name", Type.CARDIO)).thenReturn(query);
    when(query.getSingleResult()).thenReturn(trainingType);

    Optional<TrainingType> result = trainingTypeDao.findByName(Type.CARDIO);

    assertThat(result).isPresent().contains(trainingType);
    verify(query).setParameter("name", Type.CARDIO);
    verify(query).getSingleResult();
  }

  @Test
  void testFindByName_WhenTrainingTypeDoesNotExist() {
    when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
    when(query.setParameter("name", Type.CARDIO)).thenReturn(query);
    when(query.getSingleResult()).thenThrow(NoResultException.class);

    Optional<TrainingType> result = trainingTypeDao.findByName(Type.CARDIO);

    assertThat(result).isEmpty();
    verify(query).setParameter("name", Type.CARDIO);
    verify(query).getSingleResult();
  }

  @Test
  void testFindById_WhenTrainingTypeExists() {
    when(entityManager.find(TrainingType.class, 1L)).thenReturn(trainingType);

    Optional<TrainingType> result = trainingTypeDao.findById(1L);

    assertThat(result).isPresent().contains(trainingType);
    verify(entityManager).find(TrainingType.class, 1L);
  }

  @Test
  void testFindById_WhenTrainingTypeDoesNotExist() {
    when(entityManager.find(TrainingType.class, 1L)).thenReturn(null);

    Optional<TrainingType> result = trainingTypeDao.findById(1L);

    assertThat(result).isEmpty();
    verify(entityManager).find(TrainingType.class, 1L);
  }

  @Test
  void testFindAll() {
    when(entityManager.createQuery("select tt from TrainingType tt")).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(trainingType));

    List<TrainingType> result = trainingTypeDao.findAll();

    assertThat(result).containsExactly(trainingType);
    verify(entityManager).createQuery("select tt from TrainingType tt");
    verify(query).getResultList();
  }
}
