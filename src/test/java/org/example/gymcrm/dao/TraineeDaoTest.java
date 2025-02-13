package org.example.gymcrm.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.*;
import java.util.Optional;
import org.example.gymcrm.dao.impl.TraineeDaoImpl;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TraineeDaoTest {

  @Mock private EntityManager entityManager;

  @InjectMocks private TraineeDaoImpl traineeDao;

  private Trainee trainee;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUsername("testUser");

    trainee = new Trainee();
    trainee.setId(1L);
    trainee.setUser(user);
  }

  @Test
  void testSave() {
    traineeDao.save(trainee);

    verify(entityManager, times(1)).persist(trainee);
  }

  @Test
  void testFindUsernames() {
    TypedQuery<String> query = mock(TypedQuery.class);
    when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(query);
    when(query.getResultList()).thenReturn(Arrays.asList("testUser1", "testUser2"));

    List<String> usernames = traineeDao.findUsernames();

    assertEquals(2, usernames.size());
    assertTrue(usernames.contains("testUser1"));
    assertTrue(usernames.contains("testUser2"));
  }

  @Test
  void testDeleteByUsername() {
    Query query = mock(Query.class);
    when(entityManager.createQuery(anyString())).thenReturn(query);
    when(query.setParameter(anyString(), any())).thenReturn(query);

    traineeDao.deleteByUsername("testUser");

    verify(query, times(1)).executeUpdate();
  }

  @Test
  void testFindByUsername() {
    TypedQuery<Trainee> query = mock(TypedQuery.class);
    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
    when(query.setParameter(anyString(), any())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(trainee);

    Optional<Trainee> result = traineeDao.findByUsername("testUser");

    assertTrue(result.isPresent());
    assertEquals(trainee, result.get());
  }

  @Test
  void testFindByUsername_NotFound() {
    TypedQuery<Trainee> query = mock(TypedQuery.class);
    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
    when(query.setParameter(anyString(), any())).thenReturn(query);
    when(query.getSingleResult()).thenThrow(NoResultException.class);

    Optional<Trainee> result = traineeDao.findByUsername("nonExistingUser");

    assertFalse(result.isPresent());
  }

  @Test
  void testFindById() {
    when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);

    Optional<Trainee> result = traineeDao.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(trainee, result.get());
  }

  @Test
  void testFindById_NotFound() {
    when(entityManager.find(Trainee.class, 2L)).thenReturn(null);

    Optional<Trainee> result = traineeDao.findById(2L);

    assertFalse(result.isPresent());
  }

  @Test
  void testUpdate() {
    traineeDao.update(trainee);

    verify(entityManager, times(1)).merge(trainee);
  }

  @Test
  void testFindAll() {
    TypedQuery<Trainee> query = mock(TypedQuery.class);
    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
    when(query.getResultList()).thenReturn(Arrays.asList(trainee));

    List<Trainee> trainees = traineeDao.findAll();

    assertEquals(1, trainees.size());
    assertEquals(trainee, trainees.get(0));
  }
}
