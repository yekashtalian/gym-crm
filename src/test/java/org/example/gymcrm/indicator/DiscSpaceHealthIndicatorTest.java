package org.example.gymcrm.indicator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.io.File;

public class DiscSpaceHealthIndicatorTest {

  @Mock private File disk;

  private DiscSpaceHealthIndicator healthIndicator;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    healthIndicator = new DiscSpaceHealthIndicator(disk);
  }

  @Test
  public void healthShouldReturnUpWhenFreeSpaceIsAboveThreshold() {
    // Arrange
    long freeSpace = 600 * 1024 * 1024;
    when(disk.getFreeSpace()).thenReturn(freeSpace);

    // Act
    Health health = healthIndicator.health();

    // Assert
    assertEquals("UP", health.getStatus().toString());
    assertEquals("600 MB available", health.getDetails().get("freeSpace"));
  }

  @Test
  public void healthShouldReturnDownWhenFreeSpaceIsBelowThreshold() {
    // Arrange
    long freeSpace = 400 * 1024 * 1024;
    when(disk.getFreeSpace()).thenReturn(freeSpace);

    // Act
    Health health = healthIndicator.health();

    // Assert
    assertEquals("DOWN", health.getStatus().toString());
    assertEquals("Low disk space! Only 400 MB left.", health.getDetails().get("error"));
  }
}
