package org.example.gymcrm.security;

import static org.mockito.Mockito.*;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.example.gymcrm.security.filter.JwtAuthenticationFilter;
import org.example.gymcrm.security.service.JwtService;
import org.example.gymcrm.security.service.JwtBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
  @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Mock private JwtService jwtService;

  @Mock private UserDetailsService userDetailsService;

  @Mock private JwtBlacklistService blacklistService;

  @Mock private HandlerExceptionResolver resolver;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private UserDetails userDetails;

  private final String validToken = "validToken";
  private final String invalidToken = "invalidToken";

  @BeforeEach
  void setUp() {
    jwtAuthenticationFilter =
        new JwtAuthenticationFilter(jwtService, userDetailsService, blacklistService);
  }

  @Test
  void testFilterValidToken() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
    when(jwtService.extractUsername(validToken)).thenReturn("testUser");
    when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
    when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);
    when(blacklistService.isTokenBlacklisted(validToken)).thenReturn(false);

    jwtAuthenticationFilter.doFilter(request, response, filterChain);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testFilterWithoutAuthorizationHeader() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn(null);
    jwtAuthenticationFilter.doFilter(request, response, filterChain);
    verify(filterChain).doFilter(request, response);
  }
}
