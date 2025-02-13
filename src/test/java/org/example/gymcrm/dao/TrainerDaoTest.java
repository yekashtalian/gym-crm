package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.gymcrm.dao.impl.TrainerDaoImpl;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    private Trainer trainer;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);
    }

    @Test
    void testSave() {
        trainerDao.save(trainer);

        verify(entityManager, times(1)).persist(trainer);
    }

    @Test
    void testFindUsernames() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList("testUser1", "testUser2"));

        List<String> usernames = trainerDao.findUsernames();

        assertEquals(2, usernames.size());
        assertTrue(usernames.contains("testUser1"));
        assertTrue(usernames.contains("testUser2"));
    }

    @Test
    void testFindByUsername() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainer);

        Optional<Trainer> result = trainerDao.findByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void testFindByUsername_NotFound() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        Optional<Trainer> result = trainerDao.findByUsername("nonExistingUser");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindById() {
        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);

        Optional<Trainer> result = trainerDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void testFindById_NotFound() {
        when(entityManager.find(Trainer.class, 2L)).thenReturn(null);

        Optional<Trainer> result = trainerDao.findById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdate() {
        trainerDao.update(trainer);

        verify(entityManager, times(1)).merge(trainer);
    }

    @Test
    void testFindUnassignedTrainersByTraineeUsername() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainer));

        List<Trainer> trainers = trainerDao.findUnassignedTrainersByTraineeUsername("testUser");

        assertEquals(1, trainers.size());
        assertEquals(trainer, trainers.get(0));
    }

    @Test
    void testFindAll() {
        TypedQuery<Trainer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(trainer));

        List<Trainer> trainers = trainerDao.findAll();

        assertEquals(1, trainers.size());
        assertEquals(trainer, trainers.get(0));
    }
}
