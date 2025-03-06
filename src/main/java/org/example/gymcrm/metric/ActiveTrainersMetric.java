package org.example.gymcrm.metric;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActiveTrainersMetric {
  private final MeterRegistry meterRegistry;
  private final JdbcTemplate jdbcTemplate;

  public ActiveTrainersMetric(MeterRegistry meterRegistry, JdbcTemplate jdbcTemplate) {
    this.meterRegistry = meterRegistry;
    this.jdbcTemplate = jdbcTemplate;
    meterRegistry.gauge("gym_active_trainers", this, ActiveTrainersMetric::getActiveTrainers);
  }

  public int getActiveTrainers() {
    String sql =
        """
                SELECT COUNT(*) FROM trainers t
                JOIN users u ON t.trainer_id = u.id
                WHERE u.is_active = TRUE
                """;

    return jdbcTemplate.queryForObject(sql, Integer.class);
  }
}
