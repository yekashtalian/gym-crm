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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserDao userDao;

    @InjectMocks private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("oldpassword");
    }

    @Test
    void validateCredentials_ShouldReturnTrue_WhenCredentialsAreCorrect() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.validateCredentials("testuser", "oldpassword");

        assertThat(result).isTrue();
    }

    @Test
    void validateCredentials_ShouldReturnFalse_WhenPasswordIsIncorrect() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.validateCredentials("testuser", "wrongpassword");

        assertThat(result).isFalse();
    }

    @Test
    void validateCredentials_ShouldReturnFalse_WhenUserNotFound() {
        when(userDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        boolean result = userService.validateCredentials("nonexistent", "password");

        assertThat(result).isFalse();
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenOldPasswordIsCorrect() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));

        userService.changePassword("testuser", "oldpassword", "newpassword");

        assertThat(user.getPassword()).isEqualTo("newpassword");
    }

    @Test
    void changePassword_ShouldThrowException_WhenUserNotFound() {
        when(userDao.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changePassword("nonexistent", "oldpassword", "newpassword"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("This user doesn't exist");
    }

    @Test
    void changePassword_ShouldThrowException_WhenOldPasswordIsIncorrect() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.changePassword("testuser", "wrongpassword", "newpassword"))
                .isInstanceOf(UserServiceException.class)
                .hasMessage("Invalid old password");
    }

    @Test
    void changePassword_ShouldThrowException_WhenNewPasswordIsSameAsOldPassword() {
        when(userDao.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.changePassword("testuser", "oldpassword", "oldpassword"))
                .isInstanceOf(UserServiceException.class)
                .hasMessage("New password must be different from the old one");
    }
}

