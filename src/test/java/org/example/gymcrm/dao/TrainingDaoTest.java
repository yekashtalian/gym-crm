//package org.example.gymcrm.dao;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.util.*;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import org.example.gymcrm.dao.impl.TrainingDaoImpl;
//import org.example.gymcrm.entity.Training;
//import org.example.gymcrm.entity.TrainingType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class TrainingDaoTest {
//
//    @Mock
//    private EntityManager entityManager;
//
//    @InjectMocks
//    private TrainingDaoImpl trainingDao;
//
//    private Training training;
//    private Date fromDate;
//    private Date toDate;
//
//    @BeforeEach
//    void setUp() {
//        training = new Training();
//        training.setId(1L);
//        training.setDate(new Date());
//
//        fromDate = new Date(System.currentTimeMillis() - 100000);
//        toDate = new Date(System.currentTimeMillis() + 100000);
//    }
//
//    @Test
//    void testSave() {
//        trainingDao.save(training);
//
//        verify(entityManager, times(1)).persist(training);
//    }
//
//    @Test
//    void testFindById() {
//        when(entityManager.find(Training.class, 1L)).thenReturn(training);
//
//        Optional<Training> result = trainingDao.findById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(training, result.get());
//    }
//
//    @Test
//    void testFindById_NotFound() {
//        when(entityManager.find(Training.class, 2L)).thenReturn(null);
//
//        Optional<Training> result = trainingDao.findById(2L);
//
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void testGetTrainingsByTraineeUsername() {
//        TypedQuery<Training> query = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
//        when(query.setParameter(anyString(), any())).thenReturn(query);
//        when(query.getResultList()).thenReturn(Arrays.asList(training));
//
//        List<Training> trainings = trainingDao.getTrainingsByTraineeUsername("testUser", fromDate, toDate, "John");
//
//        assertEquals(1, trainings.size());
//        assertEquals(training, trainings.get(0));
//    }
//
//    @Test
//    void testGetTrainingsByTraineeUsername_WithNullParameters() {
//        TypedQuery<Training> query = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
//        when(query.setParameter(anyString(), any())).thenReturn(query);
//        when(query.getResultList()).thenReturn(Arrays.asList(training));
//
//        List<Training> trainings = trainingDao.getTrainingsByTraineeUsername("testUser", null, null, null);
//
//        assertEquals(1, trainings.size());
//        assertEquals(training, trainings.get(0));
//    }
//
//    @Test
//    void testGetTrainingsByTrainerUsername() {
//        TypedQuery<Training> query = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
//        when(query.setParameter(anyString(), any())).thenReturn(query);
//        when(query.getResultList()).thenReturn(Arrays.asList(training));
//
//        List<Training> trainings = trainingDao.getTrainingsByTrainerUsername("trainerUser", fromDate, toDate, TrainingType.Type.CARDIO, "Jane");
//
//        assertEquals(1, trainings.size());
//        assertEquals(training, trainings.get(0));
//    }
//
//    @Test
//    void testGetTrainingsByTrainerUsername_WithNullParameters() {
//        TypedQuery<Training> query = mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
//        when(query.setParameter(anyString(), any())).thenReturn(query);
//        when(query.getResultList()).thenReturn(Arrays.asList(training));
//
//        List<Training> trainings = trainingDao.getTrainingsByTrainerUsername("trainerUser", null, null, null, null);
//
//        assertEquals(1, trainings.size());
//        assertEquals(training, trainings.get(0));
//    }
//}
