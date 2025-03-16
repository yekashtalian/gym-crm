package org.example.gymcrm.security.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publishAuthenticationFailure(Authentication authentication) {
    eventPublisher.publishEvent(
        new AuthenticationFailureBadCredentialsEvent(
            authentication, new BadCredentialsException("Invalid credentials")));
  }
}
