package org.example.gymcrm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.example.gymcrm.dao.UserDao;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.UserServiceException;
import org.example.gymcrm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserDao userDao;

    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedOldPassword");
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenOldPasswordIsCorrect() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        userService.changePassword("testuser", "oldpassword", "newpassword");

        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
        verify(userDao).findByUsername("testuser");
        verify(passwordEncoder).matches("oldpassword", "encodedOldPassword");
        verify(passwordEncoder).encode("newpassword");
    }

    @Test
    void changePassword_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(
                        () ->
                                userService.changePassword(
                                        "nonexistent", "oldpassword", "newpassword"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("This user doesn't exist");

        verify(userDao).findByUsername("nonexistent");
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void changePassword_ShouldThrowUserServiceException_WhenOldPasswordIsIncorrect() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedOldPassword")).thenReturn(false);

        assertThatThrownBy(
                        () ->
                                userService.changePassword(
                                        "testuser", "wrongpassword", "newpassword"))
                .isInstanceOf(UserServiceException.class)
                .hasMessage("Invalid old password");

        verify(userDao).findByUsername("testuser");
        verify(passwordEncoder).matches("wrongpassword", "encodedOldPassword");
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void changePassword_ShouldThrowUserServiceException_WhenNewPasswordIsSameAsOldPassword() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.matches("oldpassword", "encodedOldPassword")).thenReturn(true);

        assertThatThrownBy(
                        () -> userService.changePassword("testuser", "oldpassword", "oldpassword"))
                .isInstanceOf(UserServiceException.class)
                .hasMessage("New password must be different from the old one");

        verify(userDao).findByUsername("testuser");
        verify(passwordEncoder, times(2)).matches("oldpassword", "encodedOldPassword");
        verifyNoMoreInteractions(passwordEncoder);
    }
}
