package org.example.gymcrm.indicator;

import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;

import java.util.List;

import static org.example.gymcrm.entity.TrainingType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainingTypeHealthIndicatorTest {

  @Mock private TrainingTypeDao trainingTypeDao;

  private TrainingTypeHealthIndicator healthIndicator;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    healthIndicator =
        new TrainingTypeHealthIndicator(trainingTypeDao);
  }

  @Test
  public void healthShouldReturnUpWhenTrainingTypesAreAvailable() {
    // Arrange
    when(trainingTypeDao.findAll())
        .thenReturn(mockListWithTrainingTypes());

    // Act
    Health health = healthIndicator.health();

    // Assert
    assertEquals("UP", health.getStatus().toString());
    assertEquals("Available", health.getDetails().get("trainingTypes"));
  }

  @Test
  public void healthShouldReturnDownWhenNoTrainingTypesAvailable() {
    // Arrange
    when(trainingTypeDao.findAll()).thenReturn(mockEmptyList());

    // Act
    Health health = healthIndicator.health();

    // Assert
    assertEquals("DOWN", health.getStatus().toString());
    assertEquals("No training types available", health.getDetails().get("trainingTypes"));
  }

  private List<TrainingType> mockListWithTrainingTypes() {
    return List.of(new TrainingType(Type.CARDIO), new TrainingType(Type.PILATES));
  }

  private List<TrainingType> mockEmptyList() {
    return List.of();
  }
}
