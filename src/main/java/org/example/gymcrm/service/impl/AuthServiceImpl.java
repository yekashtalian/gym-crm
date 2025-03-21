package org.example.gymcrm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.gymcrm.security.event.CustomAuthenticationEventPublisher;
import org.example.gymcrm.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void authenticate(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        log.info("Validating user {} ....", username);
        authenticationManager.authenticate(authentication);
    }
}
