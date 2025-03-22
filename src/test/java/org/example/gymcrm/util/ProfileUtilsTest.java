package org.example.gymcrm.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ProfileUtilsTest {
  private static final int PASSWORD_LENGTH = 10;

  @Test
  void generateUsername_WhenBaseUsernameIsUnique_ShouldReturnBaseUsername() {
    List<String> existingUsernames = List.of("john.doe1", "jane.smith");
    String username = ProfileUtils.generateUsername("John", "Doe", existingUsernames);
    assertEquals("john.doe2", username);
  }

  @Test
  void generateUsernameShouldAppendNumber() {
    List<String> existingUsernames = List.of("john.doe", "john.doe1", "jane.smith");
    String username = ProfileUtils.generateUsername("John", "Doe", existingUsernames);
    assertEquals("john.doe2", username);
  }

  @Test
  void generateUsernameShouldUseHighestNumber() {
    List<String> existingUsernames = List.of("john.doe", "john.doe1", "john.doe3");
    String username = ProfileUtils.generateUsername("John", "Doe", existingUsernames);
    assertEquals("john.doe4", username);
  }

  @Test
  void generateRandomPassword() {
    String password = ProfileUtils.generateRandomPassword();
    assertNotNull(password);
    assertEquals(PASSWORD_LENGTH, password.length());
  }

  @Test
  void generateRandomPasswordShouldReturnDifferentPasswords() {
    String password1 = ProfileUtils.generateRandomPassword();
    String password2 = ProfileUtils.generateRandomPassword();
    assertNotEquals(password1, password2);
  }
}
