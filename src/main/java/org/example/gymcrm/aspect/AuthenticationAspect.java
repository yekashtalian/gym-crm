package org.example.gymcrm.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.gymcrm.exception.UnauthorizedException;
import org.example.gymcrm.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {
  private final UserService userService;

  @SuppressWarnings("ConstantValue")
  @Around("@annotation(org.example.gymcrm.aspect.RequiresAuthentication)")
  public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      log.warn("Missing request context");
      throw new UnauthorizedException("Missing request context");
    }

    HttpServletRequest request = attributes.getRequest();
    String username = request.getHeader("Username");
    String password = request.getHeader("Password");

    if (username == null || password == null) {
      log.warn("Missing authentication headers");
      throw new UnauthorizedException("Missing authentication headers");
    }

    log.info("Validating credentials for user: {}", username);
    var isAuthenticated = userService.validateCredentials(username, password);
    if (!isAuthenticated) {
      log.warn("Authentication failed for user: {}", username);
      throw new UnauthorizedException("Invalid credentials");
    }

    log.info("Authentication successful for user: {}", username);
    return joinPoint.proceed();
  }
}
