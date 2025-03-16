package org.example.gymcrm.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginAttemptService {
  private static final int MAX_ATTEMPT = 3;
  @Setter private long blockTime = TimeUnit.MINUTES.toMillis(1);

  private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
  private final Map<String, Long> lockCache = new ConcurrentHashMap<>();

  @Autowired private HttpServletRequest request;

  public void loginFailed(String key) {
    int attempts = attemptsCache.getOrDefault(key, 0);
    ++attempts;
    attemptsCache.put(key, attempts);
    if (attempts >= MAX_ATTEMPT) {
      lockCache.put(key, System.currentTimeMillis());
    }
  }

  public boolean isBlocked() {
    String key = getClientIP();
    if (!lockCache.containsKey(key)) {
      return false;
    }

    long lockTime = lockCache.get(key);
    if (System.currentTimeMillis() - lockTime > blockTime) {
      lockCache.remove(key);
      attemptsCache.remove(key);
      return false;
    }

    log.info("Blocking IP: " + key);
    return true;
  }

  private String getClientIP() {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }
}
