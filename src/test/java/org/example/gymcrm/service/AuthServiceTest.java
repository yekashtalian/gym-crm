package org.example.gymcrm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.example.gymcrm.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AuthServiceImpl authService;

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
    }
    @Test
    void testAuthenticate_FailureThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.authenticate("testuser", "wrongpassword"));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
