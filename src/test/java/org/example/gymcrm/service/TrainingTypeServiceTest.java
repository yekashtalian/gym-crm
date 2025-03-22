package org.example.gymcrm.service;

import static org.example.gymcrm.entity.TrainingType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.entity.TrainingType;
import org.example.gymcrm.mapper.TrainingTypeMapper;
import org.example.gymcrm.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

  @Mock private TrainingTypeDao trainingTypeDao;

  @Mock private TrainingTypeMapper trainingTypeMapper;

  @InjectMocks private TrainingTypeServiceImpl trainingTypeService;

  private TrainingType trainingType1;
  private TrainingType trainingType2;
  private TrainingTypeDto trainingTypeDto1;
  private TrainingTypeDto trainingTypeDto2;

  @BeforeEach
  void setUp() {
    trainingType1 = new TrainingType(1L, Type.YOGA);
    trainingType2 = new TrainingType(2L, Type.PILATES);

    trainingTypeDto1 = new TrainingTypeDto("YOGA", 1L);
    trainingTypeDto2 = new TrainingTypeDto("PILATES", 2L);
  }

  @Test
  void findAll_shouldReturnListOfTrainingTypeDtos() {
    when(trainingTypeDao.findAll()).thenReturn(List.of(trainingType1, trainingType2));
    when(trainingTypeMapper.toDtoWithId(trainingType1)).thenReturn(trainingTypeDto1);
    when(trainingTypeMapper.toDtoWithId(trainingType2)).thenReturn(trainingTypeDto2);

    List<TrainingTypeDto> result = trainingTypeService.findAll();

    assertEquals(2, result.size());
    assertEquals(trainingTypeDto1, result.get(0));
    assertEquals(trainingTypeDto2, result.get(1));
    verify(trainingTypeDao, times(1)).findAll();
    verify(trainingTypeMapper, times(1)).toDtoWithId(trainingType1);
    verify(trainingTypeMapper, times(1)).toDtoWithId(trainingType2);
  }
}
