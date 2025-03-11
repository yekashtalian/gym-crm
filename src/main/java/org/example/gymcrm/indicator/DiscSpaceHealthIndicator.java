package org.example.gymcrm.indicator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiscSpaceHealthIndicator implements HealthIndicator {

  private static final long THRESHOLD = 500 * 1024 * 1024;
  private final File file;

  public DiscSpaceHealthIndicator() {
    this(new File("/"));
  }

  public DiscSpaceHealthIndicator(File file) {
    this.file = file;
  }

  @Override
  public Health health() {
    long freeSpace = file.getFreeSpace();

    if (freeSpace > THRESHOLD) {
      return Health.up()
                   .withDetail("freeSpace", freeSpace / (1024 * 1024) + " MB available")
                   .build();
    } else {
      return Health.down()
                   .withDetail("error", "Low disk space! Only " + (freeSpace / (1024 * 1024)) + " MB left.")
                   .build();
    }
  }
}
