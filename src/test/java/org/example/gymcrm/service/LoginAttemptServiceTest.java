package org.example.gymcrm.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.gymcrm.service.impl.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginAttemptServiceTest {
  private static final int MAX_ATTEMPT = 3;

  @Mock private HttpServletRequest request;

  @InjectMocks private LoginAttemptService loginAttemptService;

  @BeforeEach
  public void setUp() {
    loginAttemptService.setBlockTime(60000);
  }

  @Test
  public void testLoginFailed_IncrementsAttempts() {
    String ip = "192.168.1.1";

    loginAttemptService.loginFailed(ip);
    loginAttemptService.loginFailed(ip);

    Map<String, Integer> attemptsCache = getAttemptsCache(loginAttemptService);
    assertEquals(2, attemptsCache.get(ip));
  }

  @Test
  public void testIsBlocked_ReturnsTrueWhenBlocked() {
    String ip = "192.168.1.1";
    when(request.getRemoteAddr()).thenReturn(ip);

    for (int i = 0; i < MAX_ATTEMPT; i++) {
      loginAttemptService.loginFailed(ip);
    }

    assertTrue(loginAttemptService.isBlocked());
  }

  @Test
  public void testIsBlocked_ReturnsFalseWhenNotBlocked() {
    String ip = "192.168.1.1";
    when(request.getRemoteAddr()).thenReturn(ip);

    assertFalse(loginAttemptService.isBlocked());
  }

  @Test
  public void testIsBlocked_ReturnsFalseAfterBlockTimeExpires() {
    String ip = "192.168.1.1";
    when(request.getRemoteAddr()).thenReturn(ip);

    for (int i = 0; i < MAX_ATTEMPT; i++) {
      loginAttemptService.loginFailed(ip);
    }

    // Simulate the block time expiring by setting a shorter block time
    loginAttemptService.setBlockTime(1); // 1 millisecond
    try {
      Thread.sleep(2); // Wait for 2 milliseconds to ensure the block time expires
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    assertFalse(loginAttemptService.isBlocked());
  }

  private Map<String, Integer> getAttemptsCache(LoginAttemptService service) {
    try {
      java.lang.reflect.Field field = LoginAttemptService.class.getDeclaredField("attemptsCache");
      field.setAccessible(true);
      return (Map<String, Integer>) field.get(service);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Map<String, Long> getLockCache(LoginAttemptService service) {
    try {
      java.lang.reflect.Field field = LoginAttemptService.class.getDeclaredField("lockCache");
      field.setAccessible(true);
      return (Map<String, Long>) field.get(service);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
