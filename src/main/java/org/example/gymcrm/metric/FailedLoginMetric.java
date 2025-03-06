package org.example.gymcrm.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailedLoginMetric {
  private final Counter failedLoginCounter;

  @Autowired
  public FailedLoginMetric(MeterRegistry meterRegistry) {
    this.failedLoginCounter = meterRegistry.counter("gym_failed_login_attempts");
  }

  public void incrementFailedLogin() {
    failedLoginCounter.increment();
  }
}
