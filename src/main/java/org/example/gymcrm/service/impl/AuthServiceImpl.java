package org.example.gymcrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.security.event.CustomAuthenticationEventPublisher;
import org.example.gymcrm.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final AuthenticationManager authenticationManager;
  private final CustomAuthenticationEventPublisher eventPublisher;

  @Override
  @Transactional
  public boolean authenticate(String username, String password) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
    try {
      var authenticated = authenticationManager.authenticate(authentication);
      SecurityContextHolder.getContext().setAuthentication(authenticated);
      return true;
    } catch (AuthenticationException e) {
      eventPublisher.publishAuthenticationFailure(authentication);
      return false;
    }
  }
}
