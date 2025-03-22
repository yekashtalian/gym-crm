package org.example.gymcrm.indicator;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingTypeHealthIndicator implements HealthIndicator {
  private final TrainingTypeDao trainingTypeDao;

  @Override
  public Health health() {
    if (!trainingTypeDao.findAll().isEmpty()) {
      return Health.up().withDetail("trainingTypes", "Available").build();
    } else {
      return Health.down().withDetail("trainingTypes", "No training types available").build();
    }
  }
}
