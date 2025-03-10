package org.example.gymcrm.indicator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
  private final DataSource dataSource;

  @Override
  public Health health() {
    try (var connection = dataSource.getConnection()) {
      return Health.up()
          .withDetail("database", "Available")
          .withDetail("database url", dataSource.getConnection().getMetaData().getURL())
          .build();
    } catch (SQLException e) {
      return Health.down()
          .withDetail("database", "Unavailable")
          .withDetail("error", e.getMessage())
          .build();
    }
  }
}
