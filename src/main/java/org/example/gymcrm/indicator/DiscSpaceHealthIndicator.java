package org.example.gymcrm.indicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiscSpaceHealthIndicator implements HealthIndicator {

  private static final long THRESHOLD = 500 * 1024 * 1024;
  private final File disk;

  public DiscSpaceHealthIndicator(File disk) {
    this.disk = disk != null ? disk : new File("/");
  }

  @Override
  public Health health() {
    long freeSpace = disk.getFreeSpace();

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
