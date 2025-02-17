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
//import org.example.gymcrm.dao.impl.TraineeDaoImpl;
//import org.example.gymcrm.entity.Trainee;
//import org.example.gymcrm.entity.User;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class TraineeDaoTest {
//
//  @Mock private EntityManager entityManager;
//  @Mock private TypedQuery<String> usernameQuery;
//  @Mock private TypedQuery<Trainee> traineeQuery;
//
//  @InjectMocks private TraineeDaoImpl traineeDao;
//
//  private Trainee createTrainee(String username) {
//    User user = new User();
//    user.setUsername(username);
//    user.setPassword("password123");
//    user.setFirstName("John");
//    user.setLastName("Doe");
//    user.setActive(true);
//
//    Trainee trainee = new Trainee();
//    trainee.setUser(user);
//    return trainee;
//  }
//
//  @Test
//  void testSave() {
//    Trainee trainee = createTrainee("trainee1");
//
//    traineeDao.save(trainee);
//
//    verify(entityManager, times(1)).persist(trainee);
//  }
//
//  @Test
//  void testFindUsernames() {
//    when(entityManager.createQuery(anyString(), eq(String.class))).thenReturn(usernameQuery);
//    when(usernameQuery.getResultList()).thenReturn(List.of("trainee1", "trainee2"));
//
//    List<String> usernames = traineeDao.findUsernames();
//
//    assertThat(usernames).containsExactly("trainee1", "trainee2");
//    verify(entityManager, times(1)).createQuery(anyString(), eq(String.class));
//    verify(usernameQuery, times(1)).getResultList();
//  }
//
//  @Test
//  void testDeleteByUsername() {
//    Trainee trainee = createTrainee("trainee1");
//    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
//    when(traineeQuery.setParameter("username", "trainee1")).thenReturn(traineeQuery);
//    when(traineeQuery.getSingleResult()).thenReturn(trainee);
//
//    traineeDao.deleteByUsername("trainee1");
//
//    verify(entityManager, times(1)).remove(trainee);
//  }
//
//  @Test
//  void testFindByUsername_Found() {
//    Trainee trainee = createTrainee("trainee1");
//    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
//    when(traineeQuery.setParameter("username", "trainee1")).thenReturn(traineeQuery);
//    when(traineeQuery.getSingleResult()).thenReturn(trainee);
//
//    Optional<Trainee> result = traineeDao.findByUsername("trainee1");
//
//    assertThat(result).isPresent().contains(trainee);
//  }
//
//  @Test
//  void testFindByUsername_NotFound() {
//    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
//    when(traineeQuery.setParameter("username", "trainee1")).thenReturn(traineeQuery);
//    when(traineeQuery.getSingleResult()).thenThrow(new NoResultException());
//
//    Optional<Trainee> result = traineeDao.findByUsername("trainee1");
//
//    assertThat(result).isEmpty();
//  }
//
//  @Test
//  void testFindById_Found() {
//    Trainee trainee = createTrainee("trainee1");
//    when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);
//
//    Optional<Trainee> result = traineeDao.findById(1L);
//
//    assertThat(result).isPresent().contains(trainee);
//  }
//
//  @Test
//  void testFindById_NotFound() {
//    when(entityManager.find(Trainee.class, 1L)).thenReturn(null);
//
//    Optional<Trainee> result = traineeDao.findById(1L);
//
//    assertThat(result).isEmpty();
//  }
//
//  @Test
//  void testUpdate() {
//    Trainee trainee = createTrainee("trainee1");
//
//    traineeDao.update(trainee);
//
//    verify(entityManager, times(1)).merge(trainee);
//  }
//
//  @Test
//  void testFindAll() {
//    when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
//    when(traineeQuery.getResultList()).thenReturn(List.of(createTrainee("trainee1"), createTrainee("trainee2")));
//
//    List<Trainee> trainees = traineeDao.findAll();
//
//    assertThat(trainees).hasSize(2);
//    verify(entityManager, times(1)).createQuery(anyString(), eq(Trainee.class));
//    verify(traineeQuery, times(1)).getResultList();
//  }
//}
