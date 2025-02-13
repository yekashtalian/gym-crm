package org.example.gymcrm.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.gymcrm.dao.impl.TrainingTypeDaoImpl;
import org.example.gymcrm.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeDaoTest {

  @Mock private EntityManager entityManager;

  @InjectMocks private TrainingTypeDaoImpl trainingTypeDao;

  private TrainingType trainingType;

  @BeforeEach
  void setUp() {
    trainingType = new TrainingType();
    trainingType.setId(1L);
    trainingType.setName(TrainingType.Type.CARDIO);
  }

  @Test
  void testFindByName() {
    TypedQuery<TrainingType> query = mock(TypedQuery.class);
    when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
    when(query.setParameter(anyString(), any())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(trainingType);

    Optional<TrainingType> result = trainingTypeDao.findByName(TrainingType.Type.CARDIO);

    assertTrue(result.isPresent());
    assertEquals(trainingType, result.get());
  }

  @Test
  void testFindByName_NotFound() {
    TypedQuery<TrainingType> query = mock(TypedQuery.class);
    when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
    when(query.setParameter(anyString(), any())).thenReturn(query);
    when(query.getSingleResult()).thenThrow(NoResultException.class);

    Optional<TrainingType> result = trainingTypeDao.findByName(TrainingType.Type.CARDIO);

    assertFalse(result.isPresent());
  }
}
