package org.example.gymcrm.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import org.example.gymcrm.dao.impl.TrainingDaoImpl;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TrainingDaoTest {

  @Mock private EntityManager entityManager;

  @Mock private TypedQuery<Training> query;

  @InjectMocks private TrainingDaoImpl trainingDao;

  private Training training;
  private Trainee trainee;
  private Trainer trainer;

  @BeforeEach
  void setUp() {
    User traineeUser = new User();
    traineeUser.setUsername("trainee1");

    trainee = new Trainee();
    trainee.setUser(traineeUser);

    User trainerUser = new User();
    trainerUser.setUsername("trainer1");
    trainerUser.setFirstName("John");

    trainer = new Trainer();
    trainer.setUser(trainerUser);

    training = new Training();
    training.setId(1L);
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    training.setDate(new Date());
    var trainingType = new TrainingType();
    trainingType.setName(TrainingType.Type.CARDIO);
    training.setType(trainingType);
  }

  @Test
  void testFindAll() {
    when(entityManager.createQuery("select tr from Training tr", Training.class)).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(training));

    List<Training> result = trainingDao.findAll();

    assertThat(result).containsExactly(training);
    verify(entityManager).createQuery("select tr from Training tr", Training.class);
    verify(query).getResultList();
  }

  @Test
  void testSave() {
    trainingDao.save(training);

    ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
    verify(entityManager).persist(captor.capture());
    assertThat(captor.getValue()).isEqualTo(training);
  }

  @Test
  void testFindById() {
    when(entityManager.find(Training.class, 1L)).thenReturn(training);

    Optional<Training> result = trainingDao.findById(1L);

    assertThat(result).isPresent().contains(training);
    verify(entityManager).find(Training.class, 1L);
  }
  @Test
  void testGetTrainingsByTraineeUsername() {
    String username = "trainee1";
    Date fromDate = new Date(System.currentTimeMillis() - 1000000);
    Date toDate = new Date();
    String trainerName = "John";
    TrainingType trainingType = new TrainingType();
    trainingType.setName(TrainingType.Type.CARDIO);

    when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
    when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
    when(query.setParameter(eq("fromDate"), eq(fromDate), eq(TemporalType.DATE))).thenReturn(query);
    when(query.setParameter(eq("toDate"), eq(toDate), eq(TemporalType.DATE))).thenReturn(query);
    when(query.setParameter(eq("trainerName"), eq(trainerName))).thenReturn(query);
    when(query.setParameter(eq("trainingType"), eq(trainingType))).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(training));

    List<Training> result = trainingDao.getTrainingsByTraineeUsername(username, fromDate, toDate, trainerName, trainingType);

    assertThat(result).containsExactly(training);
    verify(entityManager).createQuery(anyString(), eq(Training.class));
    verify(query).getResultList();
  }

  @Test
  void testGetTrainingsByTrainerUsername() {
    String username = "trainer1";
    Date fromDate = new Date(System.currentTimeMillis() - 1000000);
    Date toDate = new Date();
    String traineeName = "Trainee Name";

    when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
    when(query.setParameter(eq("username"), eq(username))).thenReturn(query);
    when(query.setParameter(eq("fromDate"), eq(fromDate), eq(TemporalType.DATE))).thenReturn(query);
    when(query.setParameter(eq("toDate"), eq(toDate), eq(TemporalType.DATE))).thenReturn(query);
    when(query.setParameter(eq("traineeName"), eq(traineeName))).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(training));

    List<Training> result = trainingDao.getTrainingsByTrainerUsername(username, fromDate, toDate, traineeName);

    assertThat(result).containsExactly(training);
    verify(entityManager).createQuery(anyString(), eq(Training.class));
    verify(query).getResultList();
  }

}
