//package org.example.gymcrm.dao;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.NoResultException;
//import jakarta.persistence.TypedQuery;
//import java.util.List;
//import java.util.Optional;
//
//import org.example.gymcrm.dao.impl.TrainerDaoImpl;
//import org.example.gymcrm.entity.Trainer;
//import org.example.gymcrm.entity.User;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class TrainerDaoTest {
//
//    @Mock private EntityManager entityManager;
//    @Mock private TypedQuery<String> usernameQuery;
//    @Mock private TypedQuery<Trainer> trainerQuery;
//
//    @InjectMocks private TrainerDaoImpl trainerDao;
//
//    private Trainer createTrainer(String username) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("password123");
//        user.setFirstName("Jane");
//        user.setLastName("Doe");
//        user.setActive(true);
//
//        Trainer trainer = new Trainer();
//        trainer.setUser(user);
//        return trainer;
//    }
//
//    @Test
//    void testSave() {
//        Trainer trainer = createTrainer("trainer1");
//
//        trainerDao.save(trainer);
//
//        verify(entityManager, times(1)).persist(trainer);
//    }
//
//    @Test
//    void testFindUsernames() {
//        when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(usernameQuery);
//        when(usernameQuery.getResultList()).thenReturn(List.of("trainer1", "trainer2"));
//
//        List<String> usernames = trainerDao.findUsernames();
//
//        assertThat(usernames).containsExactly("trainer1", "trainer2");
//        verify(entityManager, times(1)).createQuery(anyString(), eq(String.class));
//        verify(usernameQuery, times(1)).getResultList();
//    }
//
//    @Test
//    void testFindByUsername_Found() {
//        Trainer trainer = createTrainer("trainer1");
//        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
//        when(trainerQuery.setParameter("username", "trainer1")).thenReturn(trainerQuery);
//        when(trainerQuery.getSingleResult()).thenReturn(trainer);
//
//        Optional<Trainer> result = trainerDao.findByUsername("trainer1");
//
//        assertThat(result).isPresent().contains(trainer);
//    }
//
//    @Test
//    void testFindByUsername_NotFound() {
//        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
//        when(trainerQuery.setParameter("username", "trainer1")).thenReturn(trainerQuery);
//        when(trainerQuery.getSingleResult()).thenThrow(new NoResultException());
//
//        Optional<Trainer> result = trainerDao.findByUsername("trainer1");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void testFindById_Found() {
//        Trainer trainer = createTrainer("trainer1");
//        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);
//
//        Optional<Trainer> result = trainerDao.findById(1L);
//
//        assertThat(result).isPresent().contains(trainer);
//    }
//
//    @Test
//    void testFindById_NotFound() {
//        when(entityManager.find(Trainer.class, 1L)).thenReturn(null);
//
//        Optional<Trainer> result = trainerDao.findById(1L);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void testUpdate() {
//        Trainer trainer = createTrainer("trainer1");
//
//        trainerDao.update(trainer);
//
//        verify(entityManager, times(1)).merge(trainer);
//    }
//
//    @Test
//    void testFindUnassignedTrainersByTraineeUsername() {
//        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
//        when(trainerQuery.setParameter("username", "trainee1")).thenReturn(trainerQuery);
//        when(trainerQuery.getResultList()).thenReturn(List.of(createTrainer("trainer1"), createTrainer("trainer2")));
//
//        List<Trainer> trainers = trainerDao.findUnassignedTrainersByTraineeUsername("trainee1");
//
//        assertThat(trainers).hasSize(2);
//        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainer.class));
//        verify(trainerQuery, times(1)).setParameter("username", "trainee1");
//        verify(trainerQuery, times(1)).getResultList();
//    }
//
//    @Test
//    void testFindAll() {
//        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
//        when(trainerQuery.getResultList()).thenReturn(List.of(createTrainer("trainer1"), createTrainer("trainer2")));
//
//        List<Trainer> trainers = trainerDao.findAll();
//
//        assertThat(trainers).hasSize(2);
//        verify(entityManager, times(1)).createQuery(anyString(), eq(Trainer.class));
//        verify(trainerQuery, times(1)).getResultList();
//    }
//}
