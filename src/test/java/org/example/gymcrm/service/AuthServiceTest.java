package org.example.gymcrm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.gymcrm.security.event.CustomAuthenticationEventPublisher;
import org.example.gymcrm.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;

    @Mock private CustomAuthenticationEventPublisher eventPublisher;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(authenticationManager, eventPublisher);
    }

    @Test
    void authenticate_ShouldReturnTrue_WhenAuthenticationIsSuccessful() {
        // Arrange
        String username = "testuser";
        String password = "password";
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // Mock successful authentication
        when(authenticationManager.authenticate(authenticationToken))
                .thenReturn(authenticationToken);

        // Act
        authService.authenticate(username, password);

        // Assert
        verify(authenticationManager)
                .authenticate(authenticationToken); // Ensure authenticate is called
        verifyNoInteractions(eventPublisher); // Ensure the eventPublisher is not called
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenAuthenticationFails() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // Create a custom subclass of AuthenticationException to throw
        AuthenticationException mockException =
                new AuthenticationException("Authentication failed") {};

        // Mock failed authentication
        when(authenticationManager.authenticate(authenticationToken)).thenThrow(mockException);

        // Act
        authService.authenticate(username, password);

        // Assert
        verify(authenticationManager)
                .authenticate(authenticationToken); // Ensure authenticate is called
        verify(eventPublisher)
                .publishAuthenticationFailure(
                        authenticationToken); // Ensure eventPublisher is called
    }
}
