package org.example.gymcrm.indicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import java.io.File;

@ExtendWith(MockitoExtension.class)
public class DiscSpaceHealthIndicatorTest {
  @Mock private File file;
  @InjectMocks private DiscSpaceHealthIndicator healthIndicator;

  @Test
  void testHealthIndicatorWithSufficientDiskSpace() {
    // Arrange
    when(file.getFreeSpace()).thenReturn(600L * 1024 * 1024); // 600 MB

    DiscSpaceHealthIndicator indicator = new DiscSpaceHealthIndicator(file);

    // Act
    Health health = indicator.health();

    // Assert
    assertEquals("UP", health.getStatus().getCode());
    assertEquals("600 MB available", health.getDetails().get("freeSpace"));
  }

  @Test
  void testHealthIndicatorWithLowDiskSpace() {
    // Arrange
    when(file.getFreeSpace()).thenReturn(400L * 1024 * 1024); // 400 MB

    DiscSpaceHealthIndicator indicator = new DiscSpaceHealthIndicator(file);

    // Act
    Health health = indicator.health();

    // Assert
    assertEquals("DOWN", health.getStatus().getCode());
    assertEquals("Low disk space! Only 400 MB left.", health.getDetails().get("error"));
  }
}
