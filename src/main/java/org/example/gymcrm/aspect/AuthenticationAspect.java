package org.example.gymcrm.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class AuthenticationAspect {
  private final UserService userService;

  @SuppressWarnings("ConstantValue")
  @Around("@annotation(org.example.gymcrm.aspect.RequiresAuthentication)")
  public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      throw new UnauthorizedException("Missing request context");
    }

    HttpServletRequest request = attributes.getRequest();
    String username = request.getHeader("Username");
    String password = request.getHeader("Password");

    if (username == null || password == null) {
      throw new UnauthorizedException("Missing authentication headers");
    }

    var isAuthenticated = userService.validateCredentials(username, password);
    if (!isAuthenticated) {
      throw new UnauthorizedException("Invalid credentials");
    }

    return joinPoint.proceed();
  }
}
